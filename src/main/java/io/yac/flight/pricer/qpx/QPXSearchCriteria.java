package io.yac.flight.pricer.qpx;

import io.yac.flight.pricer.web.resources.FlightSearchCriteria;

import java.util.List;
import java.util.stream.Collectors;

public class QPXSearchCriteria {

    private final Integer adultCount;
    private final Integer childCount;
    private final List<QPXSliceSearchCriteria> slices;
    private final Integer maximumSolutions;

    private QPXSearchCriteria(Integer adultCount, Integer childCount,
                              List<QPXSliceSearchCriteria> slices,
                              Integer maximumSolutions) {
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.slices = slices;
        this.maximumSolutions = maximumSolutions;
    }

    public static QPXSearchCriteria from(FlightSearchCriteria searchCriteria) {
        return new QPXSearchCriteria(searchCriteria.getAdultCount(), searchCriteria.getChildCount(),
                searchCriteria.getSlices().stream()
                        .map(QPXSliceSearchCriteria::from).collect(
                        Collectors.toList()),
                searchCriteria.getMaximumSolutions());
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public List<QPXSliceSearchCriteria> getSlices() {
        return slices;
    }

    public Integer getMaximumSolutions() {
        return maximumSolutions;
    }
}
