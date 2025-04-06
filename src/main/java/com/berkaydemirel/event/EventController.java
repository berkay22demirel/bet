package com.berkaydemirel.event;

import com.berkaydemirel.event.dto.EventDTO;
import com.berkaydemirel.event.dto.EventMapper;
import com.berkaydemirel.event.service.EventService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author berkaydemirel
 */
@RestController
@RequestMapping("/api/v1/events")
public class EventController {

  private final EventService service;

  private final EventMapper mapper;

  public EventController(EventService service,
                         EventMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<EventDTO> create(@RequestBody @Valid EventDTO event) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(mapper.toDto(service.create(mapper.toEntity(event))));
  }

  @GetMapping
  public ResponseEntity<List<EventDTO>> getAll() {
    return ResponseEntity.status(HttpStatus.OK)
                         .body(service.getAll().stream().map(mapper::toDto).toList());
  }
}
