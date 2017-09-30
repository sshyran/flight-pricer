package io.yac.flight.pricer.search;

import io.yac.flight.pricer.model.*;
import io.yac.flight.pricer.qpx.QPXClient;
import io.yac.flight.pricer.qpx.QPXResponse;
import io.yac.flight.pricer.qpx.QPXSearchCriteria;
import io.yac.flight.pricer.web.resources.FlightSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MultiCountrySearchHandler {


    private final QPXClient qpxClient;

    @Autowired
    public MultiCountrySearchHandler(QPXClient qpxClient) {
        this.qpxClient = qpxClient;
    }


    public QPXResponse multiCountrySearch(FlightSearchCriteria searchCriteria) {
        final QPXSearchCriteria qpxSearchCriteria = QPXSearchCriteria.from(searchCriteria);

        final List<QPXResponse> qpxResponses = searchCriteria.getTicketingCountries().parallelStream()
                .map(country -> qpxClient.searchFlights(qpxSearchCriteria, country)).collect(
                        Collectors.toList());


        Set<Carrier> dedupedCarrier = new HashSet<>();
        qpxResponses.forEach(qpxResponse -> dedupedCarrier.addAll(qpxResponse.getCarriers()));
        Set<Airport> dedupedAirports = new HashSet<>();
        qpxResponses.forEach(qpxResponse -> dedupedAirports.addAll(qpxResponse.getAirports()));
        Set<City> dedupedCities = new HashSet<>();
        qpxResponses.forEach(qpxResponse -> dedupedCities.addAll(qpxResponse.getCities()));

        List<Solution> solutionWithPriceGroupedByCountry = flatten(
                qpxResponses.stream().map(QPXResponse::getSolutions).collect(Collectors.toList()));

        return new QPXResponse(dedupedCarrier, dedupedAirports, dedupedCities, solutionWithPriceGroupedByCountry);

    }

    private List<Solution> flatten(List<List<Solution>> solutionsListList) {
        Map<String, List<Solution>> solutionMap = new HashMap<>();

        for (List<Solution> solutions : solutionsListList) {
            for (Solution solution : solutions) {
                String uniqueFlightIdentifiers = buildUniqueFlightIdentifiers(solution);
                if (!solutionMap.containsKey(uniqueFlightIdentifiers)) {
                    solutionMap.put(uniqueFlightIdentifiers, new ArrayList<>());
                }
                solutionMap.get(uniqueFlightIdentifiers).add(solution);
            }
        }

        List<Solution> flattenSolution = new ArrayList<>();

        for (Map.Entry<String, List<Solution>> solutionMapEntry : solutionMap.entrySet()) {
            List<Price> priceForCurrentSolution = new ArrayList<>();
            for (Solution solution : solutionMapEntry.getValue()) {
                priceForCurrentSolution.addAll(solution.getPrices());
            }
            Solution solution = solutionMapEntry.getValue().get(0);
            solution.setPrices(new ArrayList<>());
            solution.setPrices(priceForCurrentSolution);
            flattenSolution.add(solution);
        }

        return flattenSolution;
    }

    private String buildUniqueFlightIdentifiers(Solution solution) {
        StringBuilder uniqueFlightIdentifierBuilder = new StringBuilder();
        solution.getSlices().forEach(slice -> slice.getSegments()
                .forEach(segment -> uniqueFlightIdentifierBuilder.append(segment.getCarrierIATA())
                        .append(segment.getNumber())));
        return uniqueFlightIdentifierBuilder.toString();
    }
}
