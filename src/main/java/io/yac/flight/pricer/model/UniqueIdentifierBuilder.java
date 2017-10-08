package io.yac.flight.pricer.model;

public class UniqueIdentifierBuilder {

    public static String buildUniqueFlightIdentifiers(Solution solution) {
        final StringBuilder uniqueFlightIdentifierBuilder = new StringBuilder();
        solution.getSlices().forEach(slice -> slice.getSegments()
                .forEach(segment -> uniqueFlightIdentifierBuilder.append(segment.getCarrierIATA())
                        .append(segment.getNumber()).append(segment.getCabin())));
        return uniqueFlightIdentifierBuilder.toString();
    }
}
