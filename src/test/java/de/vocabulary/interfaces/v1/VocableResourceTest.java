package de.vocabulary.interfaces.v1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import de.vocabulary.domain.Vocable;
import de.vocabulary.test.VocableTestCatalog;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class VocableResourceTest {

  @Inject VocableTestCatalog catalog;

  @BeforeEach
  void resetCatalog() {
    catalog.deleteAll();
  }

  @Test
  void randomReturns404WhenCatalogIsEmpty() {
    given()
        .queryParam("language", "GERMAN")
        .queryParam("context", "NOUN")
        .when()
        .get("/v1/vocables/random")
        .then()
        .statusCode(404)
        .body("message", containsString("GERMAN"))
        .body("message", containsString("NOUN"));
  }

  @Test
  void randomReturnsAMatchingVocable() {
    catalog.persistNounSingular("Stadt-rest", "city-rest");

    given()
        .queryParam("language", "GERMAN")
        .queryParam("context", "NOUN")
        .when()
        .get("/v1/vocables/random")
        .then()
        .statusCode(200)
        .body("text", equalTo("Stadt-rest"))
        .body("language", equalTo("GERMAN"))
        .body("context", equalTo("NOUN"))
        .body("associates.text", hasItem("city-rest"));
  }

  @Test
  void scoreReturnsCorrectForMatchingTranslation() {
    Vocable german = catalog.persistNounSingular("Kind-rest", "child-rest");

    given()
        .contentType(ContentType.JSON)
        .body(Map.of("translation", "CHILD-rest"))
        .when()
        .post("/v1/vocables/{id}/score", german.getId())
        .then()
        .statusCode(200)
        .body("correct", equalTo(true))
        .body("associates.text", hasItem("child-rest"));
  }

  @Test
  void scoreReturnsIncorrectForUnknownTranslation() {
    Vocable german = catalog.persistNounSingular("Freund-rest", "friend-rest");

    given()
        .contentType(ContentType.JSON)
        .body(Map.of("translation", "enemy"))
        .when()
        .post("/v1/vocables/{id}/score", german.getId())
        .then()
        .statusCode(200)
        .body("correct", equalTo(false))
        .body("associates.text", hasItem("friend-rest"));
  }

  @Test
  void scoreReturns404WhenVocableIsMissing() {
    given()
        .contentType(ContentType.JSON)
        .body(Map.of("translation", "house"))
        .when()
        .post("/v1/vocables/{id}/score", UUID.randomUUID().toString())
        .then()
        .statusCode(404)
        .body("message", containsString("No vocable found with id"));
  }
}
