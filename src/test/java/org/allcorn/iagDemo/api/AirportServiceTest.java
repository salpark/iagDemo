package org.allcorn.iagDemo.api;

import org.allcorn.iagDemo.database.AirportRepository;
import org.allcorn.iagDemo.model.IATA;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AirportServiceTest {

  @Mock private AirportRepository mockAirportRepository;

  @Test
  public void returnAirportDetails() {
    AirportService underTest = new AirportService(mockAirportRepository);
    Assertions.assertThat(underTest.findByIATA(IATA.of("ABC")));
  }
}
