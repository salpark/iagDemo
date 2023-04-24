package org.allcorn.iagDemo.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.allcorn.iagDemo.model.CabinCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
public class Cabin {
  public Cabin(int bonusPercentage, CabinCode code) {
    this.bonusPercentage = bonusPercentage;
    this.code = code;
  }

  @Id @GeneratedValue private long cabin_id;

  private int bonusPercentage;

  @JdbcTypeCode(SqlTypes.VARCHAR)
  private CabinCode code;

  private String description;

  public int getBonusPercentage() {
    return bonusPercentage;
  }

  public CabinCode getCode() {
    return code;
  }
}
