package org.allcorn.iagDemo.model;

import org.immutables.value.Value;

@Value.Immutable
public interface _PointsEstimatedResult {

  Airport departFrom();

  Airport arriveAt();

  String placeholder(); // todo update to points estimate
}
