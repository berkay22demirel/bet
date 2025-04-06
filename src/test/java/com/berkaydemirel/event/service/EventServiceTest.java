package com.berkaydemirel.event.service;

import com.berkaydemirel.event.data.Event;
import com.berkaydemirel.event.data.EventRepository;
import com.berkaydemirel.event.exception.EventNotFoundException;
import com.berkaydemirel.event.exception.InvalidEventVersionException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author berkaydemirel
 */
@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

  @Mock
  private EventRepository eventRepository;

  @InjectMocks
  private EventService eventService;

  @Test
  void createShouldSaveEvent() {
    Event event = new Event();
    event.setLeagueName("Premier League");
    event.setHomeTeamName("Manchester United");
    event.setAwayTeamName("Liverpool");
    event.setHomeWinsRate(new BigDecimal("1.75"));
    event.setAwayWinsRate(new BigDecimal("2.10"));
    event.setDrawRate(new BigDecimal("3.50"));
    event.setEventDate(LocalDateTime.of(2025, 5, 12, 18, 30));

    when(eventRepository.save(event)).thenAnswer(i -> {
      Event e = ((Event) i.getArguments()[0]);
      e.setId(1L);
      e.setVersion(0);
      return e;
    });

    Event savedEvent = eventService.create(event);

    verify(eventRepository).save(event);
    assertThat(savedEvent).isEqualTo(event)
                          .returns(1L, Event::getId)
                          .returns(0, Event::getVersion)
                          .returns("Premier League", Event::getLeagueName)
                          .returns("Manchester United", Event::getHomeTeamName)
                          .returns("Liverpool", Event::getAwayTeamName)
                          .returns(new BigDecimal("1.75"), Event::getHomeWinsRate)
                          .returns(new BigDecimal("2.10"), Event::getAwayWinsRate)
                          .returns(new BigDecimal("3.50"), Event::getDrawRate)
                          .returns(LocalDateTime.of(2025, 5, 12, 18, 30), Event::getEventDate);
  }

  @Test
  void getAllShouldReturnListOfEvents() {
    Event event1 = mock(Event.class);
    Event event2 = mock(Event.class);
    when(eventRepository.findAll()).thenReturn(List.of(event1, event2));

    List<Event> events = eventService.getAll();

    verify(eventRepository).findAll();
    assertThat(events).hasSize(2)
                      .contains(event1, event2);
  }

  @Test
  void updateShouldSaveAllEvents() {
    Event event1 = mock(Event.class);
    Event event2 = mock(Event.class);
    List<Event> events = List.of(event1, event2);

    eventService.update(events);

    verify(eventRepository).saveAll(events);
  }

  @Test
  void checkEventVersionShouldDoNothingWhenVersionIsCorrect() {
    Long eventId = 1L;
    Integer version = 1;

    Event event = new Event();
    event.setVersion(version);

    when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

    eventService.checkEventVersion(eventId, version);
  }

  @Test
  void checkEventVersionShouldThrowExceptionWhenEventIsNotFound() {
    Long eventId = 1L;

    when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> eventService.checkEventVersion(eventId, 1))
        .isInstanceOf(EventNotFoundException.class)
        .hasMessage("Selected event is not found!");
  }

  @Test
  void checkEventVersionShouldThrowExceptionWhenVersionIsIncorrect() {
    Long eventId = 1L;
    Integer version = 2;

    Event event = new Event();
    event.setVersion(1);

    when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

    assertThatThrownBy(() -> eventService.checkEventVersion(eventId, version))
        .isInstanceOf(InvalidEventVersionException.class)
        .hasMessage("The selected event is not current and requires updating!");
  }
}
