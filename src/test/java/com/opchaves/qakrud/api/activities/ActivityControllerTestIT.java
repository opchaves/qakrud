package com.opchaves.qakrud.api.activities;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@QuarkusIntegrationTest
@TestMethodOrder(OrderAnnotation.class)
public class ActivityControllerTestIT {
  private static final int DEFAULT_ORDER = 0;

  @BeforeAll
  static void beforeAll() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Test
  @Order(DEFAULT_ORDER)
  void shouldListActivities() {
    given().when().get("/api/activities").then().statusCode(200).body("size()", is(0));
  }

  private static Activity createValidActivity() {
    var activity = new Activity();
    activity.title = "Test Activity";
    activity.note = "This is a test activity";
    activity.paid = true;
    activity.price = BigDecimal.valueOf(10.0);
    activity.type = ActivityType.EXPENSE;
    activity.category = "food";
    activity.handledAt = LocalDateTime.now().plusDays(1l);
    return activity;
  }

  @Test
  @Order(DEFAULT_ORDER + 1)
  void shouldCreateActivity() {
    var activity = createValidActivity();

    given().when()
        .body(activity)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .post("/api/activities")
        .then()
        .statusCode(Response.Status.CREATED.getStatusCode())
        .header(HttpHeaders.LOCATION, containsString("/api/activities/1"));

  }

  @Test
  @Order(DEFAULT_ORDER + 2)
  void shouldNotCreateInvalidActivity() {
    var activity = createValidActivity();
    activity.type = null;

    given().when()
        .body(activity)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .post("/api/activities")
        .then()
        .statusCode(BAD_REQUEST.getStatusCode());
  }
}
