package com.berkaydemirel.task;

import com.berkaydemirel.event.data.Event;
import com.berkaydemirel.event.service.EventService;
import com.berkaydemirel.shared.configuration.RandomProvider;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author berkaydemirel
 */
@Service
public class EventScheduledTasks {

  private final EventService eventService;

  private final RandomProvider randomProvider;

  public EventScheduledTasks(EventService eventService, RandomProvider randomProvider) {
    this.eventService = eventService;
    this.randomProvider = randomProvider;
  }

  @Scheduled(fixedDelay = 1000)
  public void updateEvents() {
    List<Event> updatedEvents = eventService.getAll()
                                            .stream()
                                            .filter(this::updateEvent)
                                            .toList();
    eventService.update(updatedEvents);
  }

  /**
   * A mock method that updates the rates randomly.
   */
  private boolean updateEvent(Event event) {
    if (randomProvider.getRandom()) {
      var rand = new Random();
      var remaining = new BigDecimal("6.00");
      var r1 = BigDecimal.valueOf(rand.nextDouble());
      var r2 = BigDecimal.valueOf(rand.nextDouble());
      var min = r1.min(r2);
      var max = r1.max(r2);
      event.setHomeWinsRate(BigDecimal.ONE.add(min.multiply(remaining)));
      event.setAwayWinsRate(BigDecimal.ONE.add(max.subtract(min).multiply(remaining)));
      event.setDrawRate(BigDecimal.ONE.add(BigDecimal.ONE.subtract(max).multiply(remaining)));
      return true;
    }
    return false;
  }
}