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

    return new ResponseEntity<>(
        cabinCode
            .map(
                cc ->
                    (PointsEstimatedBase)
                        PointsEstimatedResult.builder()
                            .departFrom(airportService.findByIATA(departure))
                            .arriveAt(airportService.findByIATA(destination))
                            .points(
                                cabinService
                                    .bonus(cc)
                                    .map(
                                        bonusPercent ->
                                            calculateBonus(pointsWithoutBonus, bonusPercent))
                                    .orElse(pointsWithoutBonus))
                            .build())
            .orElse(
                PointsEstimatedResultNoCabin.builder()
                    .departFrom(airportService.findByIATA(departure))
                    .arriveAt(airportService.findByIATA(destination))
                    .points(
                        cabinService.bonus().entrySet().stream()
                            .map(
                                es ->
                                    Pair.of(
                                        es.getKey(),
                                        calculateBonus(pointsWithoutBonus, es.getValue())))
                            .collect(
                                ImmutableMap.toImmutableMap(k -> k.getFirst(), v -> v.getSecond())))
                    .build()),
        HttpStatus.OK);
  }

  private long calculateBonus(long points, int bonusPercentage) {
    return (points / 100) * (100 + bonusPercentage);
  }
}
