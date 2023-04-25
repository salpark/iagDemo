package org.allcorn.iagDemo.database.model;

import jakarta.persistence.*;
import org.allcorn.iagDemo.model.AirportName;
import org.allcorn.iagDemo.model.IATA;
import org.springframework.boot.context.properties.bind.Name;

@Entity
@Table(name = "AIRPORT", indexes = @Index(name = "IDX_AIRPORT_IATA", columnList = "code"))
public class DbAirport {

  public DbAirport() {}

  public DbAirport(IATA code, AirportName name) {
    this.code = code.value();
    this.name = name.value();
  }

  @Id @GeneratedValue private long airport_id;

  private String code;

  private String name;

  public IATA getCode() {
    return IATA.of(code);
  }

  public void setCode(IATA code) {
    this.code = code.value();
  }

  public AirportName getName() {
    return AirportName.of(name);
  }

  public void setName(AirportName name) {
    this.name = name.value();
  }
}
