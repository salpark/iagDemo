package org.allcorn.iagDemo.api;

import org.allcorn.iagDemo.database.AirportRepository;
import org.allcorn.iagDemo.model.Airport;
import org.allcorn.iagDemo.model.IATA;
import org.springframework.stereotype.Component;

@Component
public class AirportService {
  private final AirportRepository airportRepository;

  public AirportService(AirportRepository airportRepository) {
    this.airportRepository = airportRepository;
  }

  public Airport findByIATA(IATA code) {
    return airportRepository.findAll().stream()
        .filter(a -> a.getCode().equals(code))
        .map(db -> Airport.builder().code(db.getCode()).name(db.getName()).build())
        .findFirst()
        .orElse(Airport.of(code));
  }
}
