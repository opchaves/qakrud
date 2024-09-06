package com.opchaves.qakrud.api.activities;

import static io.restassured.RestAssured.given;
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
import jakarta.ws.rs.core.MediaType;
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
  void testCreateActivityEndpoint() {
    var activity = new Activity();
    activity.title = "Test Activity";
    activity.note = "This is a test activity";
    activity.paid = true;
    activity.price = BigDecimal.valueOf(10.0);
    activity.type = ActivityType.EXPENSE;
    activity.category = "food";
    activity.handledAt = LocalDateTime.now().plusDays(1l);

    given().when().body(activity).contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON).post("/api/activities").then()
        .statusCode(Response.Status.CREATED.getStatusCode())
        .header(HttpHeaders.LOCATION, containsString("/api/activities/1"));

  }

  @Test
  @Order(DEFAULT_ORDER)
  void testListActivitiesEndpoint() {
    given().when().get("/api/activities").then().statusCode(200).body("size()", is(1));
  }
}
