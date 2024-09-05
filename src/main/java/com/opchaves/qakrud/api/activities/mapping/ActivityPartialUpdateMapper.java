package com.opchaves.qakrud.api.activities.mapping;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

import com.opchaves.qakrud.api.activities.Activity;

/**
 * Mapper to map <code><strong>non-null</strong></code> fields on an input
 * {@link Hero} onto a target {@link Hero}.
 */
@Mapper(componentModel = ComponentModel.JAKARTA_CDI, nullValuePropertyMappingStrategy = IGNORE)
public interface ActivityPartialUpdateMapper {

  /**
   * Maps all <code><strong>non-null</strong></code> fields from {@code input}
   * onto {@code target}.
   *
   * @param input  The input {@link Activity}
   * @param target The target {@link Activity}
   */
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void mapPartialUpdate(Activity input, @MappingTarget Activity target);

}
