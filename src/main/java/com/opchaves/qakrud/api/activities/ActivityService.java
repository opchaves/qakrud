package com.opchaves.qakrud.api.activities;

import java.util.List;

import com.opchaves.qakrud.api.activities.mapping.ActivityFullUpdateMapper;
import com.opchaves.qakrud.api.activities.mapping.ActivityPartialUpdateMapper;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

@ApplicationScoped
public class ActivityService {

  private final Validator validator;
  private final ActivityFullUpdateMapper activityFullUpdateMapper;
  private final ActivityPartialUpdateMapper activityPartialUpdateMapper;

  public ActivityService(Validator validator, ActivityFullUpdateMapper activityFullUpdateMapper,
      ActivityPartialUpdateMapper activityPartialUpdateMapper) {
    this.validator = validator;
    this.activityFullUpdateMapper = activityFullUpdateMapper;
    this.activityPartialUpdateMapper = activityPartialUpdateMapper;
  }

  public Uni<List<Activity>> findAll() {
    Log.debug("Finding all activities");
    return Activity.findAll().list();
  }

  public Uni<List<Activity>> findByCategory(String category) {
    Log.debugf("Finding activities by category = %s", category);
    return Activity.find("category", category).list();
  }

  public Uni<Activity> findActivityById(Long id) {
    Log.debugf("Finding activity by id = %d", id);
    return Activity.<Activity>findById(id).onItem().ifNull()
        .failWith(new WebApplicationException("Not Found", Status.NOT_FOUND));
  }

  @WithTransaction
  public Uni<Activity> persistActivity(Activity activity) {
    Log.debugf("Persisting activity = %s", activity);
    return Uni.createFrom().item(() -> {
      var a = new Activity();
      this.activityFullUpdateMapper.mapFullUpdate(activity, a);
      Log.debugf("Mapped activity = %s", a);
      return a;
    }).onItem().call(a -> a.persist());
  }

  @WithTransaction
  public Uni<Activity> replaceActivity(@NotNull @Valid Activity activity) {
    Log.debugf("Replacing activity = %s", activity);
    return findActivityById(activity.id).onItem().transform(a -> {
      this.activityFullUpdateMapper.mapFullUpdate(activity, a);
      return a;
    });
  }

  public Uni<Activity> partiallyUpdateActivity(@NotNull @Valid Activity activity) {
    Log.debugf("Partially updating activity = %s", activity);
    return findActivityById(activity.id).onItem().transform(a -> {
      this.activityPartialUpdateMapper.mapPartialUpdate(activity, a);
      return a;
    }).onItem().transform(this::validatePartialUpdate);
  }

  @WithTransaction
  public Uni<Void> deleteActivity(Long id) {
    Log.debugf("Deleting activity by id = %d", id);
    return Activity.deleteById(id).replaceWithVoid();
  }

  /**
   * Validates a {@link Activity} for partial update according to annotation
   * validation rules on the {@link Activity} object.
   *
   * @param hero The {@link Activity}
   * @return The same {@link Activity} that was passed in, assuming it passes
   *         validation. The return is used as a convenience so the method can be
   *         called in a functional pipeline.
   * @throws ConstraintViolationException If validation fails
   */
  private Activity validatePartialUpdate(Activity activity) {
    var violations = this.validator.validate(activity);

    if ((violations != null) && !violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    return activity;
  }
}
