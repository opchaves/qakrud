package com.opchaves.qakrud.exception;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import io.quarkus.logging.Log;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {

  private static final Set<ConstraintViolation<?>> getViolations(Throwable e) {
    if (e instanceof ConstraintViolationException) {
      return ((ConstraintViolationException) e).getConstraintViolations();
    } else if (e.getCause() instanceof ConstraintViolationException) {
      return ((ConstraintViolationException) e.getCause()).getConstraintViolations();
    }
    return null;
  }

  @Override
  public Response toResponse(Throwable e) {
    var violations = getViolations(e);

    if (violations != null) {
      var errors = violations.stream().map(violation -> {
        var field = violation.getPropertyPath().toString();
        var message = violation.getMessage();
        return new ErrorItem(field, message);
      }).toList();
      ErrorResponse errorResponse = new ErrorResponse(errors);
      return Response.status(BAD_REQUEST).entity(errorResponse).build();
    }

    if (e instanceof WebApplicationException) {
      Log.infof("WebApplicationException: {}", e.getMessage());
      Response originalResponse = ((WebApplicationException) e).getResponse();
      var error = new ErrorItem(e.getMessage());
      return Response.status(originalResponse.getStatus()).entity(new ErrorResponse(error)).build();
    }

    var id = UUID.randomUUID().toString();
    Log.errorf(e, "Error type %s with ID %s", e.getClass().getName(), id);

    String defaultErrorMessage = ResourceBundle.getBundle("ValidationMessages")
        .getString("system.error");
    var error = new ErrorItem(defaultErrorMessage);
    var errorResponse = new ErrorResponse(id, error);

    return Response.status(INTERNAL_SERVER_ERROR).entity(errorResponse).build();
  }

}
