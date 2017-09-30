package io.yac.flight.pricer.qpx;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.qpxExpress.QPXExpress;
import com.google.api.services.qpxExpress.QPXExpressRequestInitializer;
import com.google.api.services.qpxExpress.model.*;
import io.yac.flight.pricer.exceptions.DependentServiceException;
import io.yac.flight.pricer.model.*;
import io.yac.flight.pricer.repository.QpxCacheRepository;
import io.yac.flight.pricer.web.resources.FlightSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QPXClient implements SearchFlightService {

    private static final Logger LOG = LoggerFactory.getLogger(QPXClient.class);

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final HttpTransport httpTransport;
    private final QpxCacheRepository qpxCacheRepository;

    @Value("${google.api.applicationName}")
    private String applicationName;
    @Value("${google.api.key}")
    private String apiKey;

    @Value("${google.api.cachingEnabled}")
    private boolean qpxCachingEnabled;


    @Autowired
    public QPXClient(QpxCacheRepository qpxCacheRepository) throws Exception {
        this.qpxCacheRepository = qpxCacheRepository;
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }


    @Override
    public QPXResponse searchFlights(FlightSearchCriteria flightSearchCriteria) {
        PassengerCounts passengerCounts = new PassengerCounts();
        passengerCounts.setAdultCount(flightSearchCriteria.getAdultCount());
        passengerCounts.setChildCount(flightSearchCriteria.getChildCount());

        List<SliceInput> slices = new ArrayList<>();

        for (SliceSearchCriteria slice : flightSearchCriteria.getSlices()) {
            SliceInput sliceInput = new SliceInput();
            sliceInput.setOrigin(slice.getOrigin());
            sliceInput.setDestination(slice.getDestination());
            sliceInput.setPreferredCabin(slice.getCabin());
            sliceInput.setDate(slice.getDepartureDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            slices.add(sliceInput);
        }

        TripOptionsRequest request = new TripOptionsRequest();
        request.setSlice(slices);
        request.setPassengers(passengerCounts);
        request.setTicketingCountry(flightSearchCriteria.getTicketingCountry());
        request.setSolutions(flightSearchCriteria.getMaximumSolutions());

        TripsSearchRequest tripsSearchRequest = new TripsSearchRequest();
        tripsSearchRequest.setRequest(request);

        QPXExpress qpxExpress =
                new QPXExpress.Builder(httpTransport, JSON_FACTORY, null).setApplicationName(applicationName)
                        .setGoogleClientRequestInitializer(new QPXExpressRequestInitializer(apiKey)).build();

        try {
            TripsSearchResponse response = callService(tripsSearchRequest, qpxExpress);

            List<Solution> solutions = buildSolutions(response);

            Data data = response.getTrips().getData();
            return new QPXResponse(buildCarriers(data.getCarrier()), buildAirports(data.getAirport()),
                    buildCities(data.getCity()), solutions);


        } catch (IOException e) {
            LOG.error("Exception while calling QPX API with request {" + request + "} ", e);
            throw new DependentServiceException(e);
        }

    }

    private TripsSearchResponse callService(TripsSearchRequest tripsSearchRequest, QPXExpress qpxExpress)
            throws IOException {
        if (qpxCachingEnabled) {
            Optional<QpxCache> byRequestHash = qpxCacheRepository.findByRequestHash(tripsSearchRequest.hashCode());
            if (byRequestHash.isPresent()) {
                JsonParser jsonParser = JSON_FACTORY.createJsonParser(byRequestHash.get().getResponseJson());
                return jsonParser.parse(TripsSearchResponse.class);
            } else {
                TripsSearchResponse response = qpxExpress.trips().search(tripsSearchRequest).execute();
                try {
                    response.setFactory(JSON_FACTORY);
                    tripsSearchRequest.setFactory(JSON_FACTORY);

                    QpxCache cache = new QpxCache();
                    cache.setRequestHash(tripsSearchRequest.hashCode());
                    cache.setResponseJson(response.toString());
                    cache.setRequestJson(tripsSearchRequest.toString());
                    qpxCacheRepository.save(cache);

                } catch (Throwable t) {
                    LOG.info("Throwable while saving cached value for" + tripsSearchRequest, t);
                }
                return response;

            }


        } else {
            return qpxExpress.trips().search(tripsSearchRequest).execute();
        }
    }

    private List<City> buildCities(List<CityData> citiesData) {
        List<City> cities = new ArrayList<>(citiesData.size());
        for (CityData cityData : citiesData) {
            City city = new City(cityData.getCode(), cityData.getName(), cityData.getCountry());
            cities.add(city);
        }
        return cities;
    }

    private List<Airport> buildAirports(List<AirportData> airportsData) {
        List<Airport> airports = new ArrayList<>(airportsData.size());
        for (AirportData airportData : airportsData) {
            Airport airport = new Airport(airportData.getCode(), airportData.getName(), airportData.getCity());
            airports.add(airport);
        }
        return airports;

    }

    private List<Carrier> buildCarriers(List<CarrierData> carriersData) {
        List<Carrier> carriers = new ArrayList<>(carriersData.size());
        for (CarrierData carrierData : carriersData) {
            Carrier carrier = new Carrier(carrierData.getCode(), carrierData.getName());
            carriers.add(carrier);
        }
        return carriers;
    }

    private List<Solution> buildSolutions(TripsSearchResponse response) {
        List<Solution> solutions = new ArrayList<>(response.getTrips().getTripOption().size());

        long solutionCounter = 1L;

        for (TripOption tripOption : response.getTrips().getTripOption()) {
            Solution solution = new Solution();
            solution.setPrice(tripOption.getSaleTotal());
            solution.setId(solutionCounter++);

            List<Slice> slices = new ArrayList<>(tripOption.getSlice().size());
            long sliceCounter = 1L;
            for (SliceInfo sliceInfo : tripOption.getSlice()) {
                Slice slice = new Slice();
                slice.setId(1L);
                slice.setDuration(sliceInfo.getDuration());
                long segmentCounter = 1L;
                for (SegmentInfo segmentInfo : sliceInfo.getSegment()) {
                    Segment segment = new Segment();
                    segment.setBookingCode(segmentInfo.getBookingCode());
                    segment.setBookingCodeAvailableSeats(segmentInfo.getBookingCodeCount());
                    segment.setCabin(Cabin.fromQpxResponse(segmentInfo.getCabin()));
                    segment.setCarrierIATA(segmentInfo.getFlight().getCarrier());
                    segment.setFlightNumber(segmentInfo.getFlight().getNumber());
                    segment.setMarriedSegmentGroup(segmentInfo.getMarriedSegmentGroup());
                    segment.setConnectionDuration(segmentInfo.getConnectionDuration());
                    segment.setId(segmentCounter++);

                    List<Leg> legs = new ArrayList<>();
                    long legCounter = 1L;
                    for (LegInfo legInfo : segmentInfo.getLeg()) {
                        Leg leg = new Leg();
                        leg.setId(legCounter++);
                        leg.setAircraft(legInfo.getAircraft());
                        leg.setOrigin(legInfo.getOrigin());
                        leg.setDestination(legInfo.getDestination());
                        leg.setChangePlane(legInfo.getChangePlane());
                        leg.setAircraft(legInfo.getAircraft());
                        leg.setOperationDisclosure(legInfo.getOperatingDisclosure());
                        leg.setDuration(legInfo.getDuration());
                        leg.setLocalDepartureTime(LocalDateTime
                                .parse(legInfo.getDepartureTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                        leg.setLocalArrivalTime(LocalDateTime
                                .parse(legInfo.getArrivalTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));

                        legs.add(leg);
                    }
                    slice.addSegment(segment);
                }
                slices.add(slice);
            }
            solution.setSlices(slices);
            solutions.add(solution);

        }
        return solutions;
    }


}
