package org.allcorn.iagDemo.api;

import org.allcorn.iagDemo.database.CabinRepository;
import org.allcorn.iagDemo.model.CabinCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinService {

  public static final int BONUS_FOR_UNKNOWN_CABIN_CODE = 0;

  private final CabinRepository cabinRepository;

  @Autowired
  public CabinService(CabinRepository cabinRepository) {
    this.cabinRepository = cabinRepository;
  }

  public int bonus(CabinCode code) {
    return cabinRepository.findAll().stream()
        .filter(c -> c.getCode().equals(code))
        .map(c -> c.getBonusPercentage())
        .findFirst()
        .orElse(BONUS_FOR_UNKNOWN_CABIN_CODE);
  }
}
