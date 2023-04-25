package org.allcorn.iagDemo.database;

import org.allcorn.iagDemo.database.model.DbAirport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<DbAirport, Long> {}
