package io.yac.flight.pricer.qpx;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.qpxExpress.QPXExpress;
import com.google.api.services.qpxExpress.QPXExpressRequestInitializer;
import com.google.api.services.qpxExpress.model.*;
import io.yac.flight.pricer.exceptions.DependentServiceException;
import io.yac.flight.pricer.model.*;
import io.yac.flight.pricer.web.model.FlightSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class QPXClient implements SearchFlightService {

    private static final Logger LOG = LoggerFactory.getLogger(QPXClient.class);

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final HttpTransport httpTransport;

    @Value("${google.api.applicationName}")
    private String applicationName;
    @Value("${google.api.key}")
    private String apiKey;

    public QPXClient() throws Exception {
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
            TripsSearchResponse response = qpxExpress.trips().search(tripsSearchRequest).execute();

            List<Solution> solutions = buildSolutions(response);

            Data data = response.getTrips().getData();
            return new QPXResponse(buildCarriers(data.getCarrier()), buildAirports(data.getAirport()),
                    buildCities(data.getCity()), solutions);


        } catch (IOException e) {
            LOG.error("Exception while calling QPX API with request {" + request + "} ", e);
            throw new DependentServiceException(e);
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

        for (TripOption tripOption : response.getTrips().getTripOption()) {
            Solution solution = new Solution();
            solution.setPrice(tripOption.getSaleTotal());

            List<Slice> slices = new ArrayList<>(tripOption.getSlice().size());

            for (SliceInfo sliceInfo : tripOption.getSlice()) {
                Slice slice = new Slice();
                sliceInfo.setDuration(sliceInfo.getDuration());
                for (SegmentInfo segmentInfo : sliceInfo.getSegment()) {
                    Segment segment = new Segment();
                    segment.setBookingCode(segmentInfo.getBookingCode());
                    segment.setBookingCodeAvailableSeats(segmentInfo.getBookingCodeCount());
                    segment.setCabin(Cabin.fromQpxResponse(segmentInfo.getCabin()));
                    segment.setCarrierIATA(segmentInfo.getFlight().getCarrier());
                    segment.setFlightNumber(segmentInfo.getFlight().getNumber());
                    segment.setMarriedSegmentGroup(segmentInfo.getMarriedSegmentGroup());
                    segment.setConnectionDuration(segmentInfo.getConnectionDuration());

                    List<Leg> legs = new ArrayList<>();
                    for (LegInfo legInfo : segmentInfo.getLeg()) {
                        Leg leg = new Leg();
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
