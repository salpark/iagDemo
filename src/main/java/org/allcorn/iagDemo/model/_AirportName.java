package org.allcorn.iagDemo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.allcorn.iagDemo.utils.Wrapper;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = AirportName.class)
public abstract class _AirportName extends Wrapper<String> {}
