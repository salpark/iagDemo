package org.allcorn.iagDemo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = PointsEstimatedResult.class)
public interface _PointsEstimatedResult extends PointsEstimatedBase {

  Long points();
}
