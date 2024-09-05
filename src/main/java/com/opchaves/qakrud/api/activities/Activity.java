package com.opchaves.qakrud.api.activities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity(name = "activities")
@Schema(name = "activity", description = "The activity resource")
public class Activity extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "activities_id_seq", sequenceName = "activities_id_seq", initialValue = 1, allocationSize = 1)
  @GeneratedValue(generator = "activities_id_seq", strategy = GenerationType.SEQUENCE)
  public Long id;

  @NotBlank
  @Length(min = 3)
  @Schema(required = true, description = "The title of the activity", example = "Pizza")
  @Column(nullable = false)
  public String title;

  @Schema(description = "A note about the activity", example = "I love pizza")
  @Column
  public String note;

  @Schema(description = "If the activity is paid", example = "true")
  @Column
  public boolean paid;

  @Schema(description = "The price of the activity", example = "10.00")
  @Column(nullable = false)
  @Positive
  public BigDecimal price;

  @Schema(description = "The category of the activity", example = "Food")
  @Column(nullable = false)
  public String category;

  @Schema(description = "The type of the activity", example = "expense")
  @Enumerated(EnumType.STRING)
  @Column(name = "this_type", nullable = false)
  public ActivityType type;

  @Schema(description = "The date and time when the activity happened", example = "2024-09-01T10:00:00")
  @FutureOrPresent
  @Column(name = "handled_at", nullable = false)
  public LocalDateTime handledAt;

  @Schema(description = "The date and time when the activity was created", example = "2024-09-01T10:00:00")
  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  public LocalDateTime createdAt;

  @Schema(description = "The date and time when the activity was updated", example = "2024-09-01T10:00:00")
  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  public LocalDateTime updatedAt;

  public Activity() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Activity act = (Activity) o;
    return this.id.equals(act.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }
}
