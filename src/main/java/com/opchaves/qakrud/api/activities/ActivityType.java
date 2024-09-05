package com.opchaves.qakrud.api.activities;

import java.util.Locale;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonValue;

@Schema(name = "activity_type", description = "The type of the activity")
public enum ActivityType {
  INCOME,
  EXPENSE;

  /**
   * Serialises to and from lower case for jackson.
   *
   * @return lower case ActivityType name.
   */
  @JsonValue
  public String toLower() {
    return this.toString().toLowerCase(Locale.ENGLISH);
  }
}
