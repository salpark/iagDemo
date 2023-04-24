package org.allcorn.iagDemo.api;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.Stream;
import org.allcorn.iagDemo.database.RouteRepository;
import org.allcorn.iagDemo.database.model.Airport;
import org.allcorn.iagDemo.database.model.Route;
import org.allcorn.iagDemo.model.IATA;
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
public class RouteServiceTest {

  private static final IATA LHR = IATA.of("LHR");
  private static final IATA SFO = IATA.of("SFO");
  private static final IATA JFK = IATA.of("JFK");

  private static final IATA UNKNOWN_1 = IATA.of("XXX");

  private static final IATA UNKNOWN_2 = IATA.of("YYY");

  private static final long POINTS_FOR_UNKNOWN_ROUTE = 500;

  @Mock RouteRepository mockRouteRepository;

  private RouteService underTest;

  @BeforeEach
  public void setup() {
    underTest = new RouteService(mockRouteRepository);

    List<Route> allRoutes =
        ImmutableList.<Route>builder()
            .addAll(createRoute(LHR, SFO, 2000))
            .addAll(createRoute(SFO, JFK, 200))
            .addAll(createRoute(JFK, LHR, 1000))
            .build();

    Mockito.when(mockRouteRepository.findAll()).thenReturn(allRoutes);
  }

  @ParameterizedTest
  @MethodSource("correctPointsForRouteParams")
  public void selectCorrectPointsForRoute(IATA start, IATA end, long expectedPoints) {

    Assertions.assertThat(underTest.pointsForRoute(start, end)).isEqualTo(expectedPoints);
  }

  private static Stream<Arguments> correctPointsForRouteParams() {
    return Stream.of(
        Arguments.of(LHR, SFO, 2000), Arguments.of(SFO, JFK, 200), Arguments.of(JFK, LHR, 1000));
  }

  @Test
  public void unknownRoutesHaveDefaultPoints() {
    Assertions.assertThat(underTest.pointsForRoute(UNKNOWN_1, UNKNOWN_2))
        .isEqualTo(POINTS_FOR_UNKNOWN_ROUTE);
  }

  @Test
  public void outboundRouteAndReturnRouteGetTheSamePoints() {
    long outboundPoints = underTest.pointsForRoute(LHR, JFK);
    long returnPoints = underTest.pointsForRoute(JFK, LHR);

    Assertions.assertThat(outboundPoints).isEqualTo(returnPoints);
  }

  private List<Route> createRoute(IATA start, IATA end, long points) {
    Airport startAirport = new Airport(start);
    Airport endAirport = new Airport(end);

    Route outboundLeg = new Route(startAirport, endAirport, points);
    Route returnLeg = new Route(endAirport, startAirport, points);

    return ImmutableList.of(outboundLeg, returnLeg);
  }
}
