package org.allcorn.iagDemo.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import org.allcorn.iagDemo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

// import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PointsApiResourceTest {

  private static final IATA IATA_UNKNOWN_1 = IATA.of("ABC");
  private static final Airport AIRPORT_UNKNOWN_1 =
      Airport.builder().code(IATA_UNKNOWN_1).name(AirportName.of("Unknown airport 1")).build();
  private static final IATA IATA_UNKNOWN_2 = IATA.of("DEF");
  private static final Airport AIRPORT_UNKNOWN_2 =
      Airport.builder().code(IATA_UNKNOWN_2).name(AirportName.of("Unknown airport 2")).build();

  private static final CabinCode CABIN_CODE_UNKNOWN = CabinCode.of("A");
  private static final CabinCode CABIN_CODE_FIRST = CabinCode.of("F");

  private static final long POINTS_UNKNOWN_ROUTE = 500L;
  private static final long POINTS_UNKNOWN_ROUTE_FIRST_CLASS_CABIN = 1000L;
  private static final long POINTS_UNKNOWN_ROUTE_UNKNOWN_CABIN = 500L;

  private static final int BONUS_FIRST_CLASS = 100;
  private static final int BONUS_CLUB_WORLD = 50;
  private static final int BONUS_TRAVELLER_PLUS = 20;
  private static final int BONUS_TRAVELLER = 0;

  private static final String CABIN_FIRST = "First";
  private static final String CABIN_CLUB_WORLD = "Club World";
  private static final String CABIN_TRAVELLER_PLUS = "World Traveller Plus";
  private static final String CABIN_TRAVELLER = "World Traveller";

  @Mock private RouteService mockRouteService;

  @Mock private AirportService mockAirportService;

  @Mock private CabinService mockCabinService;

  private PointsApiResource underTest;

  @BeforeEach
  public void setup() {
    underTest = new PointsApiResource(mockRouteService, mockAirportService, mockCabinService);
  }

  @Test
  public void unknownAirportsWithKnownCabinCodeGiveExpectedPoints() {
    Mockito.when(mockRouteService.pointsForRoute(IATA_UNKNOWN_1, IATA_UNKNOWN_2))
        .thenReturn(POINTS_UNKNOWN_ROUTE);
    Mockito.when(mockAirportService.findByIATA(IATA_UNKNOWN_1)).thenReturn(AIRPORT_UNKNOWN_1);
    Mockito.when(mockAirportService.findByIATA(IATA_UNKNOWN_2)).thenReturn(AIRPORT_UNKNOWN_2);
    Mockito.when(mockCabinService.bonus(CABIN_CODE_FIRST))
        .thenReturn(Optional.of(BONUS_FIRST_CLASS));

    assertThat(underTest.pointsEstimateGet(IATA_UNKNOWN_1, IATA_UNKNOWN_2, CABIN_CODE_FIRST))
        .satisfies(
            result -> {
              assertThat(result.getBody())
                  .satisfies(
                      body -> {
                        PointsEstimatedResult b = (PointsEstimatedResult) body;
                        assertThat(b.points()).isEqualTo(POINTS_UNKNOWN_ROUTE_FIRST_CLASS_CABIN);
                        assertThat(b.departFrom()).isEqualTo(AIRPORT_UNKNOWN_1);
                        assertThat(b.arriveAt()).isEqualTo(AIRPORT_UNKNOWN_2);
                      });
              assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            });
  }

  @Test
  public void unknownAirportsAndUnknownCabinCodeGiveExpectedPoints() {
    Mockito.when(mockRouteService.pointsForRoute(IATA_UNKNOWN_1, IATA_UNKNOWN_2))
        .thenReturn(POINTS_UNKNOWN_ROUTE);
    Mockito.when(mockAirportService.findByIATA(IATA_UNKNOWN_1)).thenReturn(AIRPORT_UNKNOWN_1);
    Mockito.when(mockAirportService.findByIATA(IATA_UNKNOWN_2)).thenReturn(AIRPORT_UNKNOWN_2);
    Mockito.when(mockCabinService.bonus(CABIN_CODE_UNKNOWN)).thenReturn(Optional.empty());

    assertThat(underTest.pointsEstimateGet(IATA_UNKNOWN_1, IATA_UNKNOWN_2, CABIN_CODE_UNKNOWN))
        .satisfies(
            result -> {
              assertThat(result.getBody())
                  .satisfies(
                      body -> {
                        PointsEstimatedResult b = (PointsEstimatedResult) body;

                        assertThat(b.points()).isEqualTo(POINTS_UNKNOWN_ROUTE_UNKNOWN_CABIN);
                        assertThat(b.departFrom()).isEqualTo(AIRPORT_UNKNOWN_1);
                        assertThat(b.arriveAt()).isEqualTo(AIRPORT_UNKNOWN_2);
                      });
              assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            });
  }

  @Test
  public void unknownAirportsAndCabinCodeNotSupplied() {
    Mockito.when(mockRouteService.pointsForRoute(IATA_UNKNOWN_1, IATA_UNKNOWN_2))
        .thenReturn(POINTS_UNKNOWN_ROUTE);
    Mockito.when(mockAirportService.findByIATA(IATA_UNKNOWN_1)).thenReturn(AIRPORT_UNKNOWN_1);
    Mockito.when(mockAirportService.findByIATA(IATA_UNKNOWN_2)).thenReturn(AIRPORT_UNKNOWN_2);
    Mockito.when(mockCabinService.bonus())
        .thenReturn(
            ImmutableMap.<String, Integer>builder()
                .put(CABIN_FIRST, BONUS_FIRST_CLASS)
                .put(CABIN_CLUB_WORLD, BONUS_CLUB_WORLD)
                .put(CABIN_TRAVELLER_PLUS, BONUS_TRAVELLER_PLUS)
                .put(CABIN_TRAVELLER, BONUS_TRAVELLER)
                .build());

    assertThat(underTest.pointsEstimateGet(IATA_UNKNOWN_1, IATA_UNKNOWN_2, null))
        .satisfies(
            result -> {
              assertThat(result.getBody())
                  .satisfies(
                      body -> {
                        PointsEstimatedResultNoCabin b = (PointsEstimatedResultNoCabin) body;
                        assertThat(b.points())
                            .containsExactlyInAnyOrderEntriesOf(
                                ImmutableMap.<String, Long>builder()
                                    .put(CABIN_FIRST, 1000L)
                                    .put(CABIN_CLUB_WORLD, 750L)
                                    .put(CABIN_TRAVELLER_PLUS, 600L)
                                    .put(CABIN_TRAVELLER, 500L)
                                    .build());
                        assertThat(b.departFrom()).isEqualTo(AIRPORT_UNKNOWN_1);
                        assertThat(b.arriveAt()).isEqualTo(AIRPORT_UNKNOWN_2);
                      });
              assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            });
  }


  //todo knownAirportsAndCabinCodeNotSupplied

  //todo knownAirportsWithKnownCabinCode
}
