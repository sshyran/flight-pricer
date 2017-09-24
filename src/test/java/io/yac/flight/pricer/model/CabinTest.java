package io.yac.flight.pricer.model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CabinTest {

    @ParameterizedTest
    @EnumSource(Cabin.class)
    @DisplayName("FromQpxValue returns the cabin with the matching qpx value if existing")
    void testFromQpxValueSuccess(Cabin cabin) {
        assertEquals(cabin, Cabin.fromQpxResponse(cabin.getQpxValue()));
    }

    @Test
    @DisplayName("FromQpxValue throws illegalArgumentException if there is no cabin with the matching qpx name")
    void testFromQpxValueFailure() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Cabin.fromQpxResponse("any"));
        assertEquals("Unsupported cabin any", exception.getMessage());
    }
}