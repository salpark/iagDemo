package org.allcorn.iagDemo.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CabinCodeTest {

    @ParameterizedTest
    @ValueSource(strings = {"AA", "AAA", "AAAA"})
    public void rejectWrongLengthCabinCodes(String incorrectLength) {
        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                CabinCode.of(incorrectLength));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "C", "M", "W", "J", "F"})
    public void successfullyCreateValidCabinCodes(String validCode) {
        Assertions.assertThatNoException().isThrownBy(() -> CabinCode.of(validCode));
    }
}
