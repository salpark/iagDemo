package org.allcorn.iagDemo.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ROUTE")
public class DbRoute {

  public DbRoute() {}

  public DbRoute(DbAirport start_airport, DbAirport end_airport, long points) {
    this.start_airport = start_airport;
    this.end_airport = end_airport;
    this.points = points;
  }

  @Id @GeneratedValue private long route_id;

  @ManyToOne @JoinColumn private DbAirport start_airport;

  @ManyToOne @JoinColumn private DbAirport end_airport;

  private long points;

  public DbAirport getStart_airport() {
    return start_airport;
  }

  public DbAirport getEnd_airport() {
    return end_airport;
  }

  public long getPoints() {
    return points;
  }

  public void setStart_airport(DbAirport start_airport) {
    this.start_airport = start_airport;
  }

  public void setEnd_airport(DbAirport end_airport) {
    this.end_airport = end_airport;
  }

  public void setPoints(long points) {
    this.points = points;
  }
}
