package com.opchaves.qakrud.api.activities;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

@Path("/api/activities")
@Tag(name = "activities")
@Produces(APPLICATION_JSON)
public class ActivityController {

  private final ActivityService activityService;

  public ActivityController(ActivityService activityService) {
    this.activityService = activityService;
  }


  @GET
  @Operation(summary = "Find all activities", description = "Find all activities optionally filtered by category")
  @APIResponse(
      responseCode = "200",
      description = "The list of activities",
      content = @Content(
          mediaType = APPLICATION_JSON,
          schema = @Schema(implementation = Activity.class, type = SchemaType.ARRAY)
      )
  )
  public Uni<List<Activity>> findAllActivities(
      @Parameter(name = "category", description = "An optional filter parameter to filter results by category") @QueryParam("category") Optional<String> category) {

    return category
        .map(this.activityService::findByCategory)
        .orElseGet(activityService::findAll)
        .invoke(activities -> Log.debugf("Total activities found = %d", activities.size()));
  }
}
