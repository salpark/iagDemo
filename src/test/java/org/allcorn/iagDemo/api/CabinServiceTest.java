package org.allcorn.iagDemo.api;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.Stream;
import org.allcorn.iagDemo.database.CabinRepository;
import org.allcorn.iagDemo.database.model.Cabin;
import org.allcorn.iagDemo.model.CabinCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CabinServiceTest {

  private static final CabinCode M_CABINCODE = CabinCode.of("M");
  private static final CabinCode W_CABINCODE = CabinCode.of("W");
  private static final CabinCode J_CABINCODE = CabinCode.of("J");
  private static final CabinCode F_CABINCODE = CabinCode.of("F");

  private static final CabinCode UNKNOWN_CABINCODE = CabinCode.of("X");

  private static final int M_BONUS = 5;
  private static final int W_BONUS = 10;
  private static final int J_BONUS = 15;
  private static final int F_BONUS = 20;

  private static final int DEFAULT_BONUS = 0;

  private static final Cabin F_CABIN = new Cabin(F_BONUS, F_CABINCODE);
  private static final Cabin M_CABIN = new Cabin(M_BONUS, M_CABINCODE);
  private static final Cabin W_CABIN = new Cabin(W_BONUS, W_CABINCODE);
  private static final Cabin J_CABIN = new Cabin(J_BONUS, J_CABINCODE);

  private static final List<Cabin> ALL_CABIN_RECORDS =
      ImmutableList.of(F_CABIN, M_CABIN, W_CABIN, J_CABIN);
  private CabinService underTest;

  @Mock private CabinRepository mockCabinRepository;

  @BeforeEach
  public void setup() {
    underTest = new CabinService(mockCabinRepository);
    Mockito.when(mockCabinRepository.findAll()).thenReturn(ALL_CABIN_RECORDS);
  }

  @ParameterizedTest
  @MethodSource("correctBonusParams")
  public void shouldSelectCorrectBonus(String rawCabinCode, int expectedBonus) {
    Assertions.assertThat(underTest.bonus(CabinCode.of(rawCabinCode))).isEqualTo(expectedBonus);
  }

  private static Stream<Arguments> correctBonusParams() {
    return Stream.of(
        Arguments.of("M", 5), Arguments.of("W", 10), Arguments.of("J", 15), Arguments.of("F", 20));
  }

  @Test
  public void shouldReturnDefaultBonusForUnknownCabin() {
    Assertions.assertThat(underTest.bonus(UNKNOWN_CABINCODE)).isEqualTo(DEFAULT_BONUS);
  }
}
