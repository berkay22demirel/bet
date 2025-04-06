package com.berkaydemirel.task;

import com.berkaydemirel.event.data.Event;
import com.berkaydemirel.event.service.EventService;
import com.berkaydemirel.shared.configuration.RandomProvider;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author berkaydemirel
 */
@ExtendWith(MockitoExtension.class)
public class EventScheduledTasksTest {

  @Mock
  private EventService eventService;

  @Mock
  private RandomProvider randomProvider;

  @InjectMocks
  private EventScheduledTasks eventScheduledTasks;

  @Captor
  private ArgumentCaptor<List<Event>> eventListArgumentCaptor;

  @Test
  void updateEventsShouldUpdateRateWhenEventGetsAnUpdate() {
    Event event = mock(Event.class);
    List<Event> events = List.of(event);
    when(eventService.getAll()).thenReturn(events);
    when(randomProvider.getRandom()).thenReturn(true);
    doNothing().when(eventService).update(eventListArgumentCaptor.capture());

    eventScheduledTasks.updateEvents();

    List<Event> updatedEvents = eventListArgumentCaptor.getValue();
    verify(eventService).update(updatedEvents);
    assertThat(updatedEvents).hasSize(1);
    Event updatedEvent = updatedEvents.getFirst();
    verify(updatedEvent).setHomeWinsRate(any(BigDecimal.class));
    verify(updatedEvent).setAwayWinsRate(any(BigDecimal.class));
    verify(updatedEvent).setDrawRate(any(BigDecimal.class));
  }

  @Test
  void updateEventsShouldNotUpdateRateWhenEventDoesNotGetAnUpdate() {
    Event event = mock(Event.class);
    List<Event> events = List.of(event);
    when(eventService.getAll()).thenReturn(events);
    when(randomProvider.getRandom()).thenReturn(false);
    doNothing().when(eventService).update(eventListArgumentCaptor.capture());

    eventScheduledTasks.updateEvents();

    List<Event> updatedEvents = eventListArgumentCaptor.getValue();
    verify(eventService).update(updatedEvents);
    assertThat(updatedEvents).hasSize(0);
    verify(event, times(0)).setHomeWinsRate(any(BigDecimal.class));
    verify(event, times(0)).setAwayWinsRate(any(BigDecimal.class));
    verify(event, times(0)).setDrawRate(any(BigDecimal.class));
  }
}
