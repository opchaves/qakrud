package com.opchaves.qakrud.api.activities;

final class Examples {
  private Examples() {
  }

  static final String VALID_CREATE_ACTIVITY_JSON = """
      {
        "title": "Pizza",
        "note": "I love pizza",
        "paid": true,
        "price": 10.00,
        "type": "expense",
        "category": "Food"
      }
      """;

  static final String VALID_ACTIVITY_JSON = """
      {
        "id": 1,
        "title": "Pizza",
        "note": "I love pizza",
        "paid": true,
        "price": 10.00,
        "category": "Food",
        "type": "expense",
        "handledAt": "2024-08-01T00:00:00Z",
        "createdAt": "2024-08-01T00:00:00Z",
        "updatedAt": "2024-08-01T00:00:00Z",
      }
      """;

  static final String VALID_LIST_ACTIVITIES_JSON = "[" + VALID_ACTIVITY_JSON + "]";
}
