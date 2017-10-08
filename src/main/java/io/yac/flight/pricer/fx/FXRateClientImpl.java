package io.yac.flight.pricer.fx;

import io.yac.ecb.rate.client.EcbRateClient;
import io.yac.ecb.rate.client.FxQuote;
import io.yac.ecb.rate.client.FxQuoteRequest;
import io.yac.ecb.rate.client.exception.UnsupportedCurrencyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
public class FXRateClientImpl implements FXRateClient {

    private static final ConcurrentHashMap<FxQuoteRequest, FxQuote> conversionCache = new ConcurrentHashMap<>();

    private final EcbRateClient ecbRateClient;

    public FXRateClientImpl() {
        ecbRateClient = EcbRateClient.getInstance();
    }

    @Override
    public BigDecimal getRate(String baseCurrency, String quoteCurrency) {
        try {
            FxQuoteRequest request = FxQuoteRequest.builder()
                    .baseCurrency(Currency.getInstance(baseCurrency)).quoteCurrency(Currency.getInstance(quoteCurrency))
                    .build();

            if (!conversionCache.containsKey(request)) {
                FxQuote quote = ecbRateClient.getQuote(request);
                conversionCache.put(request, quote);
            }
            return conversionCache.get(request).getRate();

        } catch (UnsupportedCurrencyException e) {
            throw new IllegalArgumentException("Unsupported Currency", e);
        }
    }

    @Override
    public void resetCache() {
        conversionCache.clear();
        Currency eur = Currency.getInstance("EUR");
        Currency usd = Currency.getInstance("USD");
        Currency chf = Currency.getInstance("CHF");
        Currency cad = Currency.getInstance("CAD");
        Stream.of(FxQuoteRequest.builder().baseCurrency(eur).quoteCurrency(usd).build(),
                FxQuoteRequest.builder().baseCurrency(eur).quoteCurrency(chf).build(),
                FxQuoteRequest.builder().baseCurrency(eur).quoteCurrency(cad).build(),
                FxQuoteRequest.builder().baseCurrency(usd).quoteCurrency(eur).build(),
                FxQuoteRequest.builder().baseCurrency(usd).quoteCurrency(chf).build(),
                FxQuoteRequest.builder().baseCurrency(usd).quoteCurrency(cad).build(),
                FxQuoteRequest.builder().baseCurrency(cad).quoteCurrency(eur).build(),
                FxQuoteRequest.builder().baseCurrency(cad).quoteCurrency(chf).build(),
                FxQuoteRequest.builder().baseCurrency(cad).quoteCurrency(usd).build()
        ).forEach(r -> {
            try {
                conversionCache.put(r, ecbRateClient.getQuote(r));
            } catch (UnsupportedCurrencyException e) {
                throw new IllegalArgumentException("Unsupported Currency", e);
            }
        });


    }

}
