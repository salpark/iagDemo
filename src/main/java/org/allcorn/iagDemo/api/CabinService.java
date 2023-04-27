package org.allcorn.iagDemo.api;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import org.allcorn.iagDemo.database.CabinRepository;
import org.allcorn.iagDemo.model.CabinCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CabinService {

  private final CabinRepository cabinRepository;

  @Autowired
  public CabinService(CabinRepository cabinRepository) {
    this.cabinRepository = cabinRepository;
  }

  public Optional<Integer> bonus(CabinCode code) {
    return cabinRepository.findAll().stream()
        .filter(c -> c.getCode().equals(code))
        .map(c -> c.getBonusPercentage())
        .findFirst();
  }

  public Map<String, Integer> bonusWithDescription() {
    return cabinRepository.findAll().stream()
        .collect(ImmutableMap.toImmutableMap(k -> k.getDescription(), v -> v.getBonusPercentage()));
  }
}
