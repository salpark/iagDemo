package org.allcorn.iagDemo.database;

import org.allcorn.iagDemo.database.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {}
