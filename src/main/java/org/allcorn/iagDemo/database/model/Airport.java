package org.allcorn.iagDemo.database.model;

import jakarta.persistence.*;
import org.allcorn.iagDemo.model.IATA;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(indexes = @Index(name = "IDX_AIRPORT_IATA", columnList = "code"))
public class Airport {
  public Airport(IATA code) {
    this.code = code;
  }

  @Id @GeneratedValue private long airport_id;

  @JdbcTypeCode(SqlTypes.VARCHAR)
  private IATA code;

  private String name;

  public IATA getCode() {
    return code;
  }
}
