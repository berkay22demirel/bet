package com.berkaydemirel.event.service;

import com.berkaydemirel.event.data.Event;
import com.berkaydemirel.event.data.EventRepository;
import com.berkaydemirel.event.exception.EventNotFoundException;
import com.berkaydemirel.event.exception.InvalidEventVersionException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author berkaydemirel
 */
@Service
public class EventService {

  private final EventRepository repository;

  public EventService(EventRepository repository) {
    this.repository = repository;
  }

  public Event create(Event event) {
    return repository.save(event);
  }

  public List<Event> getAll() {
    return repository.findAll();
  }

  public void update(List<Event> events) {
    repository.saveAll(events);
  }

  public void checkEventVersion(Long eventId, Integer version) {
    Event event = repository.findById(eventId)
                            .stream()
                            .findFirst()
                            .orElseThrow(EventNotFoundException::new);
    if (!event.getVersion().equals(version)) {
      throw new InvalidEventVersionException();
    }
  }
}
