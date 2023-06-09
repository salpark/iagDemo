package org.allcorn.iagDemo.database;

import org.allcorn.iagDemo.database.model.DbCabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabinRepository extends JpaRepository<DbCabin, Long> {}
