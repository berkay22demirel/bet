package com.berkaydemirel.betslip;

import com.berkaydemirel.betslip.data.BetSlip;
import com.berkaydemirel.betslip.data.BetSlipRepository;
import com.berkaydemirel.betslip.dto.BetSlipDTO;
import com.berkaydemirel.betslip.dto.BetType;
import com.berkaydemirel.betslip.service.BetSlipService;
import com.berkaydemirel.event.data.Event;
import com.berkaydemirel.event.data.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author berkaydemirel
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BetSlipControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private BetSlipRepository betSlipRepository;

  @Autowired
  private EventRepository eventRepository;

  @MockitoSpyBean
  private BetSlipService betSlipService;

  @BeforeEach
  public void setUp() {
    betSlipRepository.deleteAll();
    eventRepository.deleteAll();
  }

  @Test
  void createShouldCreateBetSlipWhenAllParametersAreValid() throws Exception {
    Long customerId = 1L;
    Event event = addEvent();
    BetSlipDTO betSlipDTO = new BetSlipDTO(null, null, event.getId(), event.getVersion(), BetType.HOME_WIN,
                                           5, new BigDecimal("100.0"));
    MvcResult response = mockMvc.perform(post("/api/v1/betslips")
                                             .header("customer-id", customerId)
                                             .contentType("application/json")
                                             .content(objectMapper.writeValueAsString(betSlipDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(customerId))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.eventId").value(event.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.eventVersion").value(event.getVersion()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.betType").value(BetType.HOME_WIN.name()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.betCount").value("5"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.betAmount").value("100.0"))
                                .andReturn();

    Long id = ((Number) JsonPath.read(response.getResponse().getContentAsString(), "$.id")).longValue();
    Optional<BetSlip> optionalBetSlip = betSlipRepository.findById(id);
    assertThat(optionalBetSlip).isPresent()
                               .get()
                               .returns(customerId, BetSlip::getCustomerId)
                               .returns(event.getId(), BetSlip::getEventId)
                               .returns(event.getVersion(), BetSlip::getEventVersion)
                               .returns(BetType.HOME_WIN, BetSlip::getBetType)
                               .returns(5, BetSlip::getBetCount)
                               .returns(new BigDecimal("100.00"), BetSlip::getBetAmount);
  }

  @ParameterizedTest
  @CsvSource(value = {
      // Invalid Event Id
      "null, 0, 1, HOME_WIN, 5, 100.0, eventId must not be null",
      // Invalid Event Version
      "1, null, 1, HOME_WIN, 5, 100.0, eventVersion must not be null",
      // Invalid CustomerId
      "1, 0, '', HOME_WIN, 5, 100.0, Required request header 'customer-id' for method parameter type Long is present "
      + "but converted to null",
      // Invalid Bet Type
      "1, 0, 1, null, 5, 100.0, betType must not be null",
      // Invalid Bet Count
      "1, 0, 1, HOME_WIN, null, 100.0, betCount must not be null",
      // Negative Bet Count
      "1, 0, 1, HOME_WIN, -1, 100.0, betCount must be greater than 0",
      // Max Bet Count
      "1, 0, 1, HOME_WIN, 501, 100.0, betCount must be less than or equal to 500",
      // Invalid Bet Amount
      "1, 0, 1, HOME_WIN, 5, null, betAmount must not be null",
      // Min Bet Amount
      "1, 0, 1, HOME_WIN, 5, 0.0, betAmount must be greater than 0.0",
      // Max Bet Amount
      "1, 0, 1, HOME_WIN, 5, 10001.0, betAmount must be less than or equal to 10000.0"
  }, nullValues = {"null"})
  void createShouldReturnValidationErrorMessageWhenAnyParameterIsNotValid(Long eventId,
                                                                          Integer eventVersion,
                                                                          String customerId,
                                                                          BetType betType,
                                                                          Integer betCount,
                                                                          BigDecimal betAmount,
                                                                          String expectedErrorMessage)
      throws Exception {

    BetSlipDTO betSlipDTO = new BetSlipDTO(null, null, eventId, eventVersion, betType, betCount, betAmount);

    mockMvc.perform(post("/api/v1/betslips")
                        .header("customer-id", customerId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(betSlipDTO)))
           .andExpect(status().isBadRequest())
           .andExpect(
               MockMvcResultMatchers.jsonPath("$.errorMessage").value(expectedErrorMessage));
  }

  @Test
  void createShouldReturnValidationErrorMessageWhenCustomerIsInvalid() throws Exception {
    Long invalidCustomerId = 100L;
    Event event = addEvent();
    BetSlipDTO betSlipDTO = new BetSlipDTO(null, null, event.getId(), event.getVersion(), BetType.HOME_WIN,
                                           5, new BigDecimal("100.0"));
    mockMvc.perform(post("/api/v1/betslips")
                        .header("customer-id", invalidCustomerId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(betSlipDTO)))
           .andExpect(status().isBadRequest())
           .andExpect(
               MockMvcResultMatchers.jsonPath("$.errorMessage")
                                    .value("Invalid customer! Please check your customer info."));
  }

  @Test
  void createShouldReturnValidationErrorMessageWhenEventIsNotFound() throws Exception {
    Long customerId = 1L;
    Long invalidEventId = 2L;
    BetSlipDTO betSlipDTO = new BetSlipDTO(null, null, invalidEventId, 0, BetType.HOME_WIN,
                                           5, new BigDecimal("100.0"));
    mockMvc.perform(post("/api/v1/betslips")
                        .header("customer-id", customerId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(betSlipDTO)))
           .andExpect(status().isBadRequest())
           .andExpect(
               MockMvcResultMatchers.jsonPath("$.errorMessage")
                                    .value("Selected event is not found!"));
  }

  @Test
  void createShouldReturnValidationErrorMessageWhenEventVersionIsInvalid() throws Exception {
    Long customerId = 1L;
    Event event = addEvent();
    Integer invalidEventVersion = event.getVersion() + 1;
    BetSlipDTO betSlipDTO = new BetSlipDTO(null, null, event.getId(), invalidEventVersion, BetType.HOME_WIN,
                                           5, new BigDecimal("100.0"));
    mockMvc.perform(post("/api/v1/betslips")
                        .header("customer-id", customerId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(betSlipDTO)))
           .andExpect(status().isBadRequest())
           .andExpect(
               MockMvcResultMatchers.jsonPath("$.errorMessage")
                                    .value("The selected event is not current and requires updating!"));
  }

  @Test
  void createShouldNotCreateBetSlipWhenMethodDurationExceedsTimeoutPeriod() throws Exception {
    Long customerId = 1L;
    Event event = addEvent();
    BetSlipDTO betSlipDTO = new BetSlipDTO(null, null, event.getId(), event.getVersion(), BetType.HOME_WIN,
                                           5, new BigDecimal("100.0"));
    doAnswer(invocation -> {
      Thread.sleep(3000);
      return invocation.callRealMethod();
    }).when(betSlipService).create(any(), any());

    mockMvc.perform(post("/api/v1/betslips")
                        .header("customer-id", customerId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(betSlipDTO)))
           .andExpect(status().is5xxServerError())
           .andExpect(
               MockMvcResultMatchers.jsonPath("$.errorMessage")
                                    .value("An unexpected error occurred! Please try again."));

    List<BetSlip> betSlipList = betSlipRepository.findAll();
    assertThat(betSlipList).isEmpty();
  }

  private Event addEvent() {
    Event event = new Event();
    event.setLeagueName("Premier League");
    event.setHomeTeamName("Manchester United");
    event.setAwayTeamName("Liverpool");
    event.setHomeWinsRate(new BigDecimal("1.75"));
    event.setAwayWinsRate(new BigDecimal("2.10"));
    event.setDrawRate(new BigDecimal("3.50"));
    event.setEventDate(LocalDateTime.of(2025, 5, 12, 18, 30));
    return eventRepository.save(event);
  }
}
