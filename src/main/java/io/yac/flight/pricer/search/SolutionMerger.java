package io.yac.flight.pricer.search;

import io.yac.flight.pricer.model.Price;
import io.yac.flight.pricer.model.Solution;
import io.yac.flight.pricer.model.UniqueIdentifierBuilder;
import io.yac.flight.pricer.qpx.QPXResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SolutionMerger {

    public List<Solution> mergeSolutionFromDifferentCountries(List<QPXResponse> responsesInAllCountrie) {
        return flatten(
                responsesInAllCountrie.stream().map(QPXResponse::getSolutions).collect(Collectors.toList()));
    }

    private List<Solution> flatten(List<List<Solution>> solutionsListList) {
        Map<String, List<Solution>> solutionMap = groupSolutionForSameFlights(solutionsListList);

        return mergePricesForSolutionWithSameFlights(solutionMap);
    }

    private List<Solution> mergePricesForSolutionWithSameFlights(Map<String, List<Solution>> solutionMap) {
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

    private Map<String, List<Solution>> groupSolutionForSameFlights(List<List<Solution>> solutionsListList) {
        Map<String, List<Solution>> solutionMap = new HashMap<>();

        for (List<Solution> solutions : solutionsListList) {
            for (Solution solution : solutions) {
                String uniqueFlightIdentifiers = UniqueIdentifierBuilder.buildUniqueFlightIdentifiers(solution);
                if (!solutionMap.containsKey(uniqueFlightIdentifiers)) {
                    solutionMap.put(uniqueFlightIdentifiers, new ArrayList<>());
                }
                solutionMap.get(uniqueFlightIdentifiers).add(solution);
            }
        }
        return solutionMap;
    }

}
