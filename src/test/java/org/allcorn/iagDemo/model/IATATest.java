package org.allcorn.iagDemo.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class IATATest {

    @ParameterizedTest
    @ValueSource(strings = {"A", "AA", "ABCD", "ABCDE"})
    public void rejectWrongLengthCodes(String incorrectLength) {
        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
                IATA.of(incorrectLength));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC", "LHR", "LAX"})
    public void successfullyCreateValidIATACodes(String validCode) {
        Assertions.assertThatNoException().isThrownBy(() -> IATA.of(validCode));
    }
}
