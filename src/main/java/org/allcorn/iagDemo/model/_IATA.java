package org.allcorn.iagDemo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import org.allcorn.iagDemo.utils.Wrapper;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = IATA.class)
public abstract class _IATA extends Wrapper<String> {
  public static final int IATA_CODE_LENGTH = 3;

  @Value.Check
  protected void check() {
    Preconditions.checkState(value().length() == IATA_CODE_LENGTH);
  }
}
