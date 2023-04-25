package org.allcorn.iagDemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = Airport.class)
public interface _Airport {

  @Value.Parameter
  @JsonProperty("IATA")
  IATA code();

  Optional<AirportName> name();
}
