package org.allcorn.iagDemo.model;

import org.immutables.value.Value;

@Value.Immutable
public interface _Airport {

  IATA code();

  AirportName name();
}
