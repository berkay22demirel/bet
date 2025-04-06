package com.berkaydemirel.event;

import com.berkaydemirel.event.data.Event;
import com.berkaydemirel.event.data.EventRepository;
import com.berkaydemirel.event.dto.EventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author berkaydemirel
 */
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private EventRepository eventRepository;

  private static final String MAX_SIZE_STRING = "1111111111111111111111111111111111111111111111111111111111111111"
                                                + "1111111111111111111111111111111111111111111111111111111111111111"
                                                + "1111111111111111111111111111111111111111111111111111111111111111"
                                                + "1111111111111111111111111111111111111111111111111111111111111111";

  @BeforeEach
  public void setUp() {
    eventRepository.deleteAll();
  }

  @Test
  void createShouldCreateEventWhenAllParametersAreValid() throws Exception {
    MvcResult response = addEvent().andExpect(status().isCreated())
                                   .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                                   .andExpect(MockMvcResultMatchers.jsonPath("$.version").isNotEmpty())
                                   .andExpect(MockMvcResultMatchers.jsonPath("$.leagueName").value("Premier League"))
                                   .andExpect(MockMvcResultMatchers.jsonPath("$.homeTeamName")
                                                                   .value("Manchester United"))
                                   .andExpect(MockMvcResultMatchers.jsonPath("$.awayTeamName").value("Liverpool"))
                                   .andExpect(MockMvcResultMatchers.jsonPath("$.homeWinsRate").value("1.75"))
                                   .andExpect(MockMvcResultMatchers.jsonPath("$.awayWinsRate").value("2.1"))
                                   .andExpect(MockMvcResultMatchers.jsonPath("$.drawRate").value("3.5"))
                                   .andExpect(MockMvcResultMatchers.jsonPath("$.eventDate")
                                                                   .value("2025-05-12T18:30:00"))
                                   .andReturn();

    Long id = ((Number) JsonPath.read(response.getResponse().getContentAsString(), "$.id")).longValue();
    Optional<Event> optionalEvent = eventRepository.findById(id);
    assertThat(optionalEvent).isPresent()
                             .get()
                             .returns("Premier League", Event::getLeagueName)
                             .returns("Manchester United", Event::getHomeTeamName)
                             .returns("Liverpool", Event::getAwayTeamName)
                             .returns(new BigDecimal("1.75"), Event::getHomeWinsRate)
                             .returns(new BigDecimal("2.10"), Event::getAwayWinsRate)
                             .returns(new BigDecimal("3.50"), Event::getDrawRate)
                             .returns(LocalDateTime.of(2025, 5, 12, 18, 30), Event::getEventDate);
  }

  @ParameterizedTest
  @CsvSource(value = {
      // Invalid League Name
      "2023-12-25T15:00:00, null, Manchester United, Liverpool, 1.8, 3.6, 4.1, leagueName must not be blank",
      // Max size League Name
      "2023-12-25T15:00:00, " + MAX_SIZE_STRING + ", Manchester United, Liverpool, 1.8, 3.6, 4.1, leagueName size must"
      + " be between 0 and 255",
      // Invalid Home Team Name
      "2023-12-25T15:00:00, Premier League, null, Liverpool, 1.8, 3.6, 4.1, homeTeamName must not be blank",
      // Max size League Name
      "2023-12-25T15:00:00, Premier League, " + MAX_SIZE_STRING + ", Liverpool, 1.8, 3.6, 4.1, homeTeamName size must"
      + " be between 0 and 255",
      // Invalid Away Team Name
      "2023-12-25T15:00:00, Premier League, Manchester United, null, 1.8, 3.6, 4.1, awayTeamName must not be blank",
      // Max size League Name
      "2023-12-25T15:00:00, Premier League, Manchester United, " + MAX_SIZE_STRING + ", 1.8, 3.6, 4.1, awayTeamName "
      + "size must be between 0 and 255",
      // Null Home Wins Rate
      "2023-12-25T15:00:00, Premier League, Manchester United, Liverpool, null, 3.6, 4.1, homeWinsRate must not be "
      + "null",
      // Invalid Home Wins Rate
      "2023-12-25T15:00:00, Premier League, Manchester United, Liverpool, 0.0, 3.6, 4.1, homeWinsRate must be greater"
      + " than 0.0",
      // Null Away Wins Rate
      "2023-12-25T15:00:00, Premier League, Manchester United, Liverpool, 1.8, null, 4.1, awayWinsRate must not be "
      + "null",
      // Invalid Home Wins Rate
      "2023-12-25T15:00:00, Premier League, Manchester United, Liverpool, 1.8, 0.0, 4.1, awayWinsRate must be greater"
      + " than 0.0",
      // Null Home Wins Rate
      "2023-12-25T15:00:00, Premier League, Manchester United, Liverpool, 1.8, 3.6, null, drawRate must not be null",
      // Invalid Home Wins Rate
      "2023-12-25T15:00:00, Premier League, Manchester United, Liverpool, 1.8, 3.6, 0.0, drawRate must be greater "
      + "than 0.0",
      // Null Event Date
      "null, Premier League, Manchester United, Liverpool, 1.8, 3.6, 4.1, eventDate must not be null"
  }, nullValues = {"null"})
  void createShouldReturnValidationErrorMessageWhenAnyParameterIsNotValid(LocalDateTime eventDate,
                                                                          String leagueName,
                                                                          String homeTeamName,
                                                                          String awayTeamName,
                                                                          BigDecimal homeWinsRate,
                                                                          BigDecimal awayWinsRate,
                                                                          BigDecimal drawRate,
                                                                          String expectedErrorMessage)
      throws Exception {

    EventDTO eventDTO = new EventDTO(null, null, leagueName, homeTeamName, awayTeamName, homeWinsRate,
                                     awayWinsRate, drawRate, eventDate);

    mockMvc.perform(post("/api/v1/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventDTO)))
           .andExpect(status().isBadRequest())
           .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(expectedErrorMessage));
  }

  @Test
  void getAllEventsShouldReturnAllEvents() throws Exception {
    addEvent().andExpect(status().isCreated());
    addEvent().andExpect(status().isCreated());

    mockMvc.perform(get("/api/v1/events"))
           .andExpect(status().isOk())
           .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNotEmpty())
           .andExpect(MockMvcResultMatchers.jsonPath("$[0].version").isNotEmpty())
           .andExpect(MockMvcResultMatchers.jsonPath("$[0].leagueName").value("Premier League"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeTeamName").value("Manchester United"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayTeamName").value("Liverpool"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeWinsRate").value("1.75"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayWinsRate").value("2.1"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[0].drawRate").value("3.5"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventDate").value("2025-05-12T18:30:00"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").isNotEmpty())
           .andExpect(MockMvcResultMatchers.jsonPath("$[1].version").isNotEmpty())
           .andExpect(MockMvcResultMatchers.jsonPath("$[1].leagueName").value("Premier League"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[1].homeTeamName").value("Manchester United"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[1].awayTeamName").value("Liverpool"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[1].homeWinsRate").value("1.75"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[1].awayWinsRate").value("2.1"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[1].drawRate").value("3.5"))
           .andExpect(MockMvcResultMatchers.jsonPath("$[1].eventDate").value("2025-05-12T18:30:00"));
  }

  private ResultActions addEvent() throws Exception {
    String eventJson = "{"
                       + "\"leagueName\": \"Premier League\","
                       + "\"homeTeamName\": \"Manchester United\","
                       + "\"awayTeamName\": \"Liverpool\","
                       + "\"homeWinsRate\": 1.75,"
                       + "\"awayWinsRate\": 2.10,"
                       + "\"drawRate\": 3.50,"
                       + "\"eventDate\": \"2025-05-12T18:30:00\""
                       + "}";

    return mockMvc.perform(post("/api/v1/events")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(eventJson));
  }
}