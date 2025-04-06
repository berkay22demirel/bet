package com.berkaydemirel.event.dto;

import com.berkaydemirel.event.data.Event;
import org.mapstruct.Mapper;

/**
 * @author berkaydemirel
 */
@Mapper(componentModel = "spring")
public interface EventMapper {

  EventDTO toDto(Event event);

  Event toEntity(EventDTO eventDTO);
}
