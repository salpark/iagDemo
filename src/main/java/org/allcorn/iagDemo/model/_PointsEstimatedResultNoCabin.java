package org.allcorn.iagDemo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Map;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = PointsEstimatedResultNoCabin.class)
public interface _PointsEstimatedResultNoCabin extends PointsEstimatedBase {

  Map<String, Long> points();
}
