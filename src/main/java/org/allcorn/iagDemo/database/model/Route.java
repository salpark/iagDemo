package org.allcorn.iagDemo.database.model;

import jakarta.persistence.*;

@Entity
public class Route {

  @Id @GeneratedValue private long route_id;

  @ManyToOne @JoinColumn private Airport start_airport;

  @ManyToOne @JoinColumn private Airport end_airport;

  private long points;
}
