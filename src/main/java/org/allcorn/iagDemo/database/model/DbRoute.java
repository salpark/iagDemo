package org.allcorn.iagDemo.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ROUTE")
public class DbRoute {

  @Id @GeneratedValue private long route_id;

  @ManyToOne @JoinColumn private DbAirport startAirport;

  @ManyToOne @JoinColumn private DbAirport endAirport;

  private long points;

  public DbRoute() {}

  public DbRoute(DbAirport startAirport, DbAirport endAirport, long points) {
    this.startAirport = startAirport;
    this.endAirport = endAirport;
    this.points = points;
  }

  public DbAirport getStartAirport() {
    return startAirport;
  }

  public DbAirport getEndAirport() {
    return endAirport;
  }

  public long getPoints() {
    return points;
  }

  public void setStartAirport(DbAirport startAirport) {
    this.startAirport = startAirport;
  }

  public void setEndAirport(DbAirport endAirport) {
    this.endAirport = endAirport;
  }

  public void setPoints(long points) {
    this.points = points;
  }
}
