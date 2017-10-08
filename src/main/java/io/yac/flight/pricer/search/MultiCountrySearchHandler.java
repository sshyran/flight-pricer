package io.yac.flight.pricer.search;

import io.yac.flight.pricer.fx.FXRateClient;
import io.yac.flight.pricer.model.*;
import io.yac.flight.pricer.qpx.QPXClient;
import io.yac.flight.pricer.qpx.QPXResponse;
import io.yac.flight.pricer.qpx.QPXSearchCriteria;
import io.yac.flight.pricer.web.resources.FlightSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MultiCountrySearchHandler {


    private final QPXClient qpxClient;
    private final SolutionMerger solutionMerger;
    private final FXRateClient fxRateClient;

    @Autowired
    public MultiCountrySearchHandler(QPXClient qpxClient, SolutionMerger solutionMerger,
                                     FXRateClient fxRateClient) {
        this.qpxClient = qpxClient;
        this.solutionMerger = solutionMerger;
        this.fxRateClient = fxRateClient;
    }


    public QPXResponse multiCountrySearch(FlightSearchCriteria searchCriteria) {
        final QPXSearchCriteria qpxSearchCriteria = QPXSearchCriteria.from(searchCriteria);

        final List<QPXResponse> responseInAllCountries = searchCriteria.getTicketingCountries().parallelStream()
                .map(country -> qpxClient.searchFlights(qpxSearchCriteria, country)).collect(
                        Collectors.toList());


        Set<Carrier> dedupedCarrier = getUniqueCarriers(responseInAllCountries);
        Set<Airport> dedupedAirports = getUniqueAirports(responseInAllCountries);
        Set<City> dedupedCities = getUniqueCities(responseInAllCountries);

        List<Solution> solutionWithPriceGroupedByCountry =
                solutionMerger.mergeSolutionFromDifferentCountries(responseInAllCountries);

        for (Solution solution : solutionWithPriceGroupedByCountry) {
            for (Price price : solution.getPrices()) {
                price.setRequestCurrency(searchCriteria.getCurrency().getCurrencyCode());
                if (price.getCurrency().equals(searchCriteria.getCurrency().getCurrencyCode())) {
                    price.setRequestCurrencyAmount(price.getAmount());

                } else {
                    BigDecimal rate =
                            fxRateClient.getRate(price.getCurrency(), searchCriteria.getCurrency().getCurrencyCode());
                    Double priceInSearchCurrency =
                            rate.multiply(BigDecimal.valueOf(price.getAmount())).setScale(2, BigDecimal.ROUND_HALF_UP)
                                    .doubleValue();
                    price.setRequestCurrencyAmount(priceInSearchCurrency);
                }
            }
        }

        return new QPXResponse(dedupedCarrier, dedupedAirports, dedupedCities, solutionWithPriceGroupedByCountry);

    }

    private Set<City> getUniqueCities(List<QPXResponse> responseInAllCountries) {
        Set<City> dedupedCities = new HashSet<>();
        responseInAllCountries.forEach(qpxResponse -> dedupedCities.addAll(qpxResponse.getCities()));
        return dedupedCities;
    }

    private Set<Airport> getUniqueAirports(List<QPXResponse> responseInAllCountries) {
        Set<Airport> dedupedAirports = new HashSet<>();
        responseInAllCountries.forEach(qpxResponse -> dedupedAirports.addAll(qpxResponse.getAirports()));
        return dedupedAirports;
    }

    private Set<Carrier> getUniqueCarriers(List<QPXResponse> responseInAllCountries) {
        Set<Carrier> dedupedCarrier = new HashSet<>();
        responseInAllCountries.forEach(qpxResponse -> dedupedCarrier.addAll(qpxResponse.getCarriers()));
        return dedupedCarrier;
    }


}
