package org.allcorn.iagDemo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import java.util.regex.Pattern;
import org.allcorn.iagDemo.utils.Wrapper;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = IATA.class)
public abstract class _IATA extends Wrapper<String> {
  public static final int IATA_CODE_LENGTH = 3;

  public final Pattern allUppercase = Pattern.compile("^[A-Z]{3}$");

  @Value.Check
  _IATA check() {
    Preconditions.checkState(value().length() == IATA_CODE_LENGTH);
    if (!allUppercase.matcher(value()).matches()) {
      return IATA.of(value().toUpperCase());
    }
    return this;
  }
}
