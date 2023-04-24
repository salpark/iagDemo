package org.allcorn.iagDemo.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.allcorn.iagDemo.model.CabinCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
public class Cabin {

    @Id
    @GeneratedValue
    private long Id;

    private int bonusPercentage;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private CabinCode code;

    private String description;

}
