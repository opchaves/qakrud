package com.opchaves.qakrud.api.activities.mapping;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

import com.opchaves.qakrud.api.activities.Activity;

/**
 * Mapper to map all fields on an input {@link Activity} onto a target
 * {@link Activity}.
 */
@Mapper(componentModel = ComponentModel.JAKARTA_CDI, imports = { LocalDateTime.class })
public interface ActivityFullUpdateMapper {

  /**
   * Maps all fields except <code>id</code> from {@code input} onto
   * {@code target}.
   *
   * @param input  The input {@link Activity}
   * @param target The target {@link Activity}
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "handledAt", defaultExpression = "java(LocalDateTime.now())")
  void mapFullUpdate(Activity input, @MappingTarget Activity target);
}
