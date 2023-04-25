package org.allcorn.iagDemo.api;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import org.allcorn.iagDemo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PointsApiResource implements PointsApiDelegate {

  private final RouteService routeService;
  private final AirportService airportService;

  private final CabinService cabinService;

  @Autowired
  public PointsApiResource(
      RouteService routeService, AirportService airportService, CabinService cabinService) {
    this.routeService = routeService;
    this.airportService = airportService;
    this.cabinService = cabinService;
  }

  @Override
  public ResponseEntity<PointsEstimatedBase> pointsEstimateGet(
      IATA departure, IATA destination, CabinCode rawCabinCode) {

    Optional<CabinCode> cabinCode = Optional.ofNullable(rawCabinCode);

    long pointsWithoutBonus = routeService.pointsForRoute(departure, destination);

    //    PointsEstimatedResultNoCabin yy;
    Optional<PointsEstimatedBase> result =
        cabinCode.map(
            cc -> {
              // to do calculate bonus
              long pointsIncludingBonus =
                  cabinService
                      .bonus(cc)
                      .map(bonusPercent -> calculateBonus(pointsWithoutBonus, bonusPercent))
                      .orElse(pointsWithoutBonus);

              return PointsEstimatedResult.builder()
                  .departFrom(airportService.findByIATA(departure))
                  .arriveAt(airportService.findByIATA(destination))
                  .points(pointsIncludingBonus)
                  .build();
            });

    //todo this is clearly a bit crap, need to refactor.
    PointsEstimatedBase zz =
        PointsEstimatedResultNoCabin.builder()
            .departFrom(airportService.findByIATA(departure))
            .arriveAt(airportService.findByIATA(destination))
            .points(
                cabinService.bonus().entrySet().stream()
                    .map(
                        es ->
                            Pair.of(es.getKey(), calculateBonus(pointsWithoutBonus, es.getValue())))
                    .collect(ImmutableMap.toImmutableMap(k -> k.getFirst(), v -> v.getSecond())))
            .build();

    return new ResponseEntity<>(result.isPresent() ? result.get() : zz, HttpStatus.OK);
    //            .orElse({
    //                     PointsEstimatedResultNoCabin yy = PointsEstimatedResultNoCabin.builder()
    //                            .departFrom(airportService.findByIATA(departure))
    //                            .arriveAt(airportService.findByIATA(destination))
    //                            .points(cabinService.bonus().entrySet().stream()
    //                                    .map(es -> Pair.of(es.getKey(),
    //                                            calculateBonus(pointsWithoutBonus,
    // es.getValue())))
    //                                    .collect(ImmutableMap.toImmutableMap(k -> k.getFirst(),
    //                                            v -> v.getSecond()))).build();
    //                    return (PointsEstimatedBase) yy ;
    //            });
    // .build());

    ////    PointsEstimatedResult result =
    //    PointsEstimatedResult.Builder builder = PointsEstimatedResult.builder()
    //            .departFrom(airportService.findByIATA(departure))
    //            .arriveAt(airportService.findByIATA(destination));
    //
    //    cabinService.bonus(cabinCode).ifPresentOrElse(b ->
    //            builder.points(b),
    //            () -> builder.pointsForAllCabins(ImmutableMap.<String, Long>of("first", 20000L,
    //                    "cattle", 2L)));

    // if options points is there
    //    builder.points()
    //            .points(Either.left(routeService.pointsForRoute(departure, destination)))
    //            .build();
    //        PointsEstimatedResult.builder().points(100L).build();

    //    return new ResponseEntity<>(result, HttpStatus.OK);
    //    return null;

  }

  private long calculateBonus(long points, int bonusPercentage) {
    return (points / 100) * (100 + bonusPercentage);
  }
}
