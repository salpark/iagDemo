package org.allcorn.iagDemo;

import com.google.common.collect.ImmutableMap;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import java.util.Optional;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IagDemoApplicationTests {

  private static final String LHR = "LHR";
  private static final String JFK = "JFK";
  private static final String YYZ = "YYZ";
  private static final String LGW = "LGW";
  private static final String SFO = "SFO";
  private static final String LAX = "LAX";

  private static final String YYZ_LOWER = "yyz";
  private static final String LGW_LOWER = "lgw";

  private static final String UNKNOWN_IATA = "XXX";
  private static final String DEPARTURE = "departure";
  private static final String DESTINATION = "destination";
  private static final String CABIN_CODE = "cabinCode";
  private static final String PATH = "points/estimate";

  private static final String INVALID_CABIN_CODE = "#";

  private static final String POINTS = "points";

  private static final String DEPART_IATA = "departFrom.IATA.value";
  private static final String ARRIVE_IATA = "arriveAt.IATA.value";

  private static final String DEPART_NAME = "departFrom.name.value";
  private static final String ARRIVE_NAME = "arriveAt.name.value";

  @LocalServerPort private int serverPort;

  @BeforeEach
  public void setup() {
    RestAssured.port = serverPort;
    RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig());
  }

  @Test
  @DisplayName("Correctly report points LHR to JFK First class")
  void correctPointsForLHRtoJFKFirstClass() {
    Assertions.assertThat(getEndpointResponse(LHR, JFK, "F"))
        .satisfies(
            jp -> {
              Assertions.assertThat(jp.getObject(POINTS, Long.class)).isEqualTo(6400);
              Assertions.assertThat(jp.getObject(DEPART_IATA, String.class)).isEqualTo("LHR");
              Assertions.assertThat(jp.getObject(DEPART_NAME, String.class))
                  .isEqualTo("London Heathrow");
              Assertions.assertThat(jp.getObject(ARRIVE_IATA, String.class)).isEqualTo("JFK");
              Assertions.assertThat(jp.getObject(ARRIVE_NAME, String.class))
                  .isEqualTo("John F Kennedy International");
            });
  }

  @Test
  @DisplayName("Correctly report points when all supplied parameters are in lower case")
  public void correctPointsForAllLowercaseParams() {
    Assertions.assertThat(getEndpointResponse(YYZ_LOWER, LGW_LOWER, "m"))
        .satisfies(
            jp -> {
              Assertions.assertThat(jp.getObject(POINTS, Long.class)).isEqualTo(3250);
              Assertions.assertThat(jp.getObject(DEPART_IATA, String.class)).isEqualTo("YYZ");
              Assertions.assertThat(jp.getObject(DEPART_NAME, String.class))
                  .isEqualTo("Lester B. Pearson International");
              Assertions.assertThat(jp.getObject(ARRIVE_IATA, String.class)).isEqualTo("LGW");
              Assertions.assertThat(jp.getObject(ARRIVE_NAME, String.class))
                  .isEqualTo("London Gatwick");
            });
  }

  @Test
  @DisplayName("Correctly report points LGW to YYZ World Traveller Plus")
  public void correctPointsForLGWtoYYZTravellerPlus() {
    Assertions.assertThat(getEndpointResponse(LGW, YYZ, "W"))
        .satisfies(
            jp -> {
              Assertions.assertThat(jp.getObject(POINTS, Long.class)).isEqualTo(3900);
              Assertions.assertThat(jp.getObject(DEPART_IATA, String.class)).isEqualTo("LGW");
              Assertions.assertThat(jp.getObject(DEPART_NAME, String.class))
                  .isEqualTo("London Gatwick");
              Assertions.assertThat(jp.getObject(ARRIVE_IATA, String.class)).isEqualTo("YYZ");
              Assertions.assertThat(jp.getObject(ARRIVE_NAME, String.class))
                  .isEqualTo("Lester B. Pearson International");
            });
  }

  @Test
  @DisplayName("Correctly report points unknown airport to YYZ Club world")
  public void correctPointsForUnknownAirportToLHRClubWorld() {
    Assertions.assertThat(getEndpointResponse(UNKNOWN_IATA, YYZ, "J"))
        .satisfies(
            jp -> {
              Assertions.assertThat(jp.getObject(POINTS, Long.class)).isEqualTo(750);
              Assertions.assertThat(jp.getObject(DEPART_IATA, String.class))
                  .isEqualTo(UNKNOWN_IATA);
              Assertions.assertThat(jp.getObject(ARRIVE_IATA, String.class)).isEqualTo("YYZ");
              Assertions.assertThat(jp.getObject(ARRIVE_NAME, String.class))
                  .isEqualTo("Lester B. Pearson International");
            });
  }

  @Test
  @DisplayName("Return all possible cabin bonus points if no cabin code supplied")
  public void allCabinsReportedLHRtoSFONoCabinCode() {
    Assertions.assertThat(getEndpointResponse(LHR, SFO))
        .satisfies(
            jp -> {
              Assertions.assertThat(jp.getObject(DEPART_IATA, String.class)).isEqualTo("LHR");
              Assertions.assertThat(jp.getObject(DEPART_NAME, String.class))
                  .isEqualTo("London Heathrow");
              Assertions.assertThat(jp.getObject(ARRIVE_IATA, String.class)).isEqualTo("SFO");
              Assertions.assertThat(jp.getObject(ARRIVE_NAME, String.class))
                  .isEqualTo("San Francisco International");
              Assertions.assertThat(jp.getMap(POINTS))
                  .containsExactlyInAnyOrderEntriesOf(
                      ImmutableMap.builder()
                          .put("World Traveller", 4400)
                          .put("World Traveller Plus", 5280)
                          .put("Club World", 6600)
                          .put("First", 8800)
                          .build());
            });
  }

  @Test
  @DisplayName("Treat an unknown cabin code as 0% bonus")
  public void allCabinsReportedLAXtoLHRInvalidCabinCode() {
    Assertions.assertThat(getEndpointResponse(LAX, LHR, INVALID_CABIN_CODE))
        .satisfies(
            jp -> {
              Assertions.assertThat(jp.getObject(DEPART_IATA, String.class)).isEqualTo("LAX");
              Assertions.assertThat(jp.getObject(DEPART_NAME, String.class))
                  .isEqualTo("Los Angeles International");
              Assertions.assertThat(jp.getObject(ARRIVE_IATA, String.class)).isEqualTo("LHR");
              Assertions.assertThat(jp.getObject(ARRIVE_NAME, String.class))
                  .isEqualTo("London Heathrow");
              Assertions.assertThat(jp.getObject(POINTS, Long.class)).isEqualTo(4500);
            });
  }

  private JsonPath getEndpointResponse(String depart, String arrive) {
    return getEndpointResponse(depart, arrive, Optional.empty());
  }

  private JsonPath getEndpointResponse(String depart, String arrive, String cabin) {
    return getEndpointResponse(depart, arrive, Optional.of(cabin));
  }

  private JsonPath getEndpointResponse(String depart, String arrive, Optional<String> cabin) {

    RequestSpecification baseSpec =
        RestAssured.given()
            .when()
            .accept(ContentType.JSON)
            .queryParam(DEPARTURE, depart)
            .queryParam(DESTINATION, arrive);

    return cabin
        .map(c -> baseSpec.queryParam(CABIN_CODE, c))
        .orElse(baseSpec)
        .get(PATH)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .jsonPath();
  }
}
