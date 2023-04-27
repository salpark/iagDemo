package org.allcorn.iagDemo.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.allcorn.iagDemo.database.CabinRepository;
import org.allcorn.iagDemo.database.model.DbCabin;
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

  private static final String F_BONUS_DESCRIPTION = "First";
  private static final String M_BONUS_DESCRIPTION = "World Traveller";
  private static final String W_BONUS_DESCRIPTION = "World Traveller Plus";
  private static final String J_BONUS_DESCRIPTION = "Club World";

  private static final DbCabin F_CABIN = new DbCabin(F_BONUS, F_CABINCODE, F_BONUS_DESCRIPTION);
  private static final DbCabin M_CABIN = new DbCabin(M_BONUS, M_CABINCODE, M_BONUS_DESCRIPTION);
  private static final DbCabin W_CABIN = new DbCabin(W_BONUS, W_CABINCODE, W_BONUS_DESCRIPTION);
  private static final DbCabin J_CABIN = new DbCabin(J_BONUS, J_CABINCODE, J_BONUS_DESCRIPTION);

  private static final List<DbCabin> ALL_CABIN_RECORDS =
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
    Assertions.assertThat(underTest.bonus(CabinCode.of(rawCabinCode)))
        .isEqualTo(Optional.of(expectedBonus));
  }

  private static Stream<Arguments> correctBonusParams() {
    return Stream.of(
        Arguments.of("M", 5), Arguments.of("W", 10), Arguments.of("J", 15), Arguments.of("F", 20));
  }

  @Test
  public void shouldReturnEmptyOptionalForUnknownCabin() {
    Assertions.assertThat(underTest.bonus(UNKNOWN_CABINCODE)).isEmpty();
  }

  @Test
  public void getAllBonusesWithDescription() {
    Assertions.assertThat(underTest.bonusWithDescription())
        .containsExactlyInAnyOrderEntriesOf(
            (Map)
                ImmutableMap.builder()
                    .put(F_BONUS_DESCRIPTION, F_BONUS)
                    .put(M_BONUS_DESCRIPTION, M_BONUS)
                    .put(W_BONUS_DESCRIPTION, W_BONUS)
                    .put(J_BONUS_DESCRIPTION, J_BONUS)
                    .build());
  }
}
