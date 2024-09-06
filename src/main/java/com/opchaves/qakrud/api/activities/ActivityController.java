package com.opchaves.qakrud.api.activities;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/api/activities")
@Tag(name = "activities")
@Produces(APPLICATION_JSON)
public class ActivityController {

  private final ActivityService activityService;

  public ActivityController(ActivityService activityService) {
    this.activityService = activityService;
  }

  @GET
  @Operation(
    summary = "Find all activities",
    description = "Find all activities optionally filtered by category")
  @APIResponse(
    responseCode = "200",
    description = "The list of activities",
    content = @Content(
      mediaType = APPLICATION_JSON,
      schema = @Schema(implementation = Activity.class, type = SchemaType.ARRAY)))
  public Uni<List<Activity>> findAllActivities(@Parameter(
    name = "category",
    description = "An optional filter parameter to filter results by category") @QueryParam("category") Optional<String> category) {

    return category.map(this.activityService::findByCategory).orElseGet(activityService::findAll)
        .invoke(activities -> Log.debugf("Total activities found = %d", activities.size()));
  }

  @POST
  @Consumes(APPLICATION_JSON)
  @Operation(summary = "Create an activity")
  @APIResponse(
    responseCode = "201",
    description = "The URI of the created activity",
    headers = @Header(name = HttpHeaders.LOCATION, schema = @Schema(implementation = URI.class)))
  @APIResponse(
    responseCode = "400",
    description = "Invalid activity passed in (or no request body found)")
  public Uni<Response> createActivity(
      @RequestBody(
        name = "activity",
        required = true,
        content = @Content(
          mediaType = APPLICATION_JSON,
          schema = @Schema(implementation = Activity.class),
          examples = @ExampleObject(
            name = "valid_activity",
            value = Examples.VALID_CREATE_ACTIVITY_JSON))) @Valid @NotNull Activity activity,
      @Context UriInfo uriInfo) {
    return activityService.persistActivity(activity).map(a -> {
      var uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(a.id)).build();
      Log.debugf("New Activity created with URI %s", uri);
      return Response.created(uri).build();
    });
  }
}
