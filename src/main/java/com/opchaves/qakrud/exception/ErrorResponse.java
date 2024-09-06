package com.opchaves.qakrud.exception;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ErrorResponse
 *
 * @see https://developers.redhat.com/articles/2022/03/03/rest-api-error-modeling-quarkus-20
 */
@Tag(name = "errorResponse")
public class ErrorResponse {
  @JsonInclude(NON_NULL)
  @Schema(description = "Unique error identifier")
  private String errorId;

  @Schema(description = "A list of errors")
  private List<ErrorItem> errors;

  public ErrorResponse(String errorId, ErrorItem error) {
    this.errorId = errorId;
    this.errors = List.of(error);
  }

  public ErrorResponse(ErrorItem error) {
    this(null, error);
  }

  public ErrorResponse(List<ErrorItem> errors) {
    this.errorId = null;
    this.errors = errors;
  }

  public ErrorResponse() {
  }

  public String getErrorId() {
    return errorId;
  }

  public List<ErrorItem> getErrors() {
    return errors;
  }
}
