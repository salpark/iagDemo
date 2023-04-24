package org.allcorn.iagDemo.database.model;

import jakarta.persistence.*;

@Entity
public class Route {
  public Route(Airport start_airport, Airport end_airport, long points) {
    this.start_airport = start_airport;
    this.end_airport = end_airport;
    this.points = points;
  }

  @Id @GeneratedValue private long route_id;

  @ManyToOne @JoinColumn private Airport start_airport;

  @ManyToOne @JoinColumn private Airport end_airport;

  private long points;

  public Airport getStart_airport() {
    return start_airport;
  }

  public Airport getEnd_airport() {
    return end_airport;
  }

  public long getPoints() {
    return points;
  }
}
