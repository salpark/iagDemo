package org.allcorn.iagDemo.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.allcorn.iagDemo.model.CabinCode;

@Entity
@Table(name = "CABIN")
public class DbCabin {

  @Id @GeneratedValue private long cabin_id;

  private int bonusPercentage;

  private String code;

  private String description;

  public DbCabin() {}

  public DbCabin(int bonusPercentage, CabinCode code, String description) {
    this.bonusPercentage = bonusPercentage;
    this.code = code.value();
    this.description = description;
  }

  public int getBonusPercentage() {
    return bonusPercentage;
  }

  public CabinCode getCode() {
    return CabinCode.of(code);
  }

  public void setCode(CabinCode code) {
    this.code = code.value();
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
