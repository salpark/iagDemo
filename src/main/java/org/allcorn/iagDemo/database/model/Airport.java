package org.allcorn.iagDemo.database.model;

import jakarta.persistence.*;
import org.allcorn.iagDemo.model.IATA;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(indexes = @Index(name = "IDX_AIRPORT_IATA", columnList = "code"))
public class Airport {

  public Airport() {}

  public Airport(IATA code) {
    this.code = code.value();
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
}
