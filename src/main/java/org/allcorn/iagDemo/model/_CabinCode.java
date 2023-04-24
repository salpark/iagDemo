package org.allcorn.iagDemo.model;

import com.google.common.base.Preconditions;
import org.allcorn.iagDemo.utils.Wrapper;
import org.immutables.value.Value;

@Value.Immutable
public abstract class _CabinCode extends Wrapper<String> {
  public static final int CABIN_CODE_LENGTH = 1;

  @Value.Check
  protected void check() {
    Preconditions.checkState(value().length() == CABIN_CODE_LENGTH);
  }

  @Value.Check
  _CabinCode forceConsistentCase() {
    if (Character.isLowerCase(value().charAt(0))) {
      return CabinCode.of(value().toUpperCase());
    }
    return this;
  }
}
