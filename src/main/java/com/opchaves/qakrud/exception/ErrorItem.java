package com.opchaves.qakrud.exception;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.fasterxml.jackson.annotation.JsonInclude;

@Tag(name = "error")
public class ErrorItem {
  @JsonInclude(NON_NULL)
  @Schema(description = "The field that caused the error")
  private String field;
  @Schema(required = true, description = "The error message")
  private String message;

  public ErrorItem(String field, String message) {
    this.field = field;
    this.message = message;
  }

  public ErrorItem(String message) {
    this(null, message);
  }

  public ErrorItem() {
  }

  public String getField() {
    return field;
  }

  public String getMessage() {
    return message;
  }
}
