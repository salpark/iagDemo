package org.allcorn.iagDemo.api;

import org.allcorn.iagDemo.database.RouteRepository;
import org.allcorn.iagDemo.model.IATA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

  private static final long POINTS_FOR_UNKNOWN_ROUTE = 500;
  private final RouteRepository routeRepository;

  @Autowired
  public RouteService(RouteRepository routeRepository) {
    this.routeRepository = routeRepository;
  }

  public long pointsForRoute(IATA start, IATA end) {

    return routeRepository.findAll().stream()
        .filter(
            r ->
                r.getStart_airport().getCode().equals(start)
                    && r.getEnd_airport().getCode().equals(end))
        .map(r -> r.getPoints())
        .findFirst()
        .orElse(POINTS_FOR_UNKNOWN_ROUTE);
  }
}
