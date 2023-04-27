package org.allcorn.iagDemo.api;

import org.allcorn.iagDemo.database.RouteRepository;
import org.allcorn.iagDemo.model.IATA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouteService {

  // todo fix name
  private static final long POINTS_FOR_UNKNOWN_ROUTEyy = 500;
  private final RouteRepository routeRepository;

  @Autowired
  public RouteService(RouteRepository routeRepository) {
    this.routeRepository = routeRepository;
  }

  public long pointsForRoute(IATA start, IATA end) {
    return routeRepository.findAll().stream()
        .filter(
            r ->
                r.getStartAirport().getCode().equals(start)
                    && r.getEndAirport().getCode().equals(end))
        .map(r -> r.getPoints())
        .findFirst()
        .orElse(POINTS_FOR_UNKNOWN_ROUTEyy);
  }
}
