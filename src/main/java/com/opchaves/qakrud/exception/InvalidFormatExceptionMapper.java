package com.opchaves.qakrud.exception;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidFormatExceptionMapper implements ExceptionMapper<InvalidFormatException> {

  @Override
  public Response toResponse(InvalidFormatException e) {
    var field = e.getPath()
        .stream()
        .map(p -> p.getFieldName())
        .reduce((a, b) -> a + "." + b)
        .orElse("unknown");
    var value = e.getValue();
    var error = new ErrorItem(field, "Value (" + value + ") could not be parsed");

    Log.infof("Path \"%s\" with value \"%s\" could not be parsed", field, value);

    return Response.status(BAD_REQUEST).entity(new ErrorResponse(error)).build();
  }
}
