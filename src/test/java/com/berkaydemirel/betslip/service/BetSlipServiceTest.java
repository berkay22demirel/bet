package com.berkaydemirel.betslip.service;

import com.berkaydemirel.betslip.data.BetSlip;
import com.berkaydemirel.betslip.data.BetSlipRepository;
import com.berkaydemirel.betslip.dto.BetType;
import com.berkaydemirel.customer.exception.InvalidCustomerException;
import com.berkaydemirel.customer.service.CustomerService;
import com.berkaydemirel.event.exception.InvalidEventVersionException;
import com.berkaydemirel.event.service.EventService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author berkaydemirel
 */
@ExtendWith(MockitoExtension.class)
public class BetSlipServiceTest {

  @Mock
  private BetSlipRepository repository;

  @Mock
  private EventService eventService;

  @Mock
  private CustomerService customerService;

  @InjectMocks
  private BetSlipService betSlipService;

  @Test
  void createShouldSaveBetSlipWhenControlsAreSuccessful() throws Exception {
    Long customerId = 1L;
    Long eventId = 2L;
    Integer eventVersion = 0;
    BetSlip betSlip = new BetSlip();
    betSlip.setEventId(eventId);
    betSlip.setEventVersion(eventVersion);
    betSlip.setBetAmount(new BigDecimal("100"));
    betSlip.setBetCount(5);
    betSlip.setBetType(BetType.HOME_WIN);
    doNothing().when(customerService).checkCustomer(customerId);
    doNothing().when(eventService).checkEventVersion(eventId, eventVersion);
    when(repository.save(betSlip)).thenReturn(betSlip);

    BetSlip createdBetSlip = betSlipService.create(betSlip, customerId);

    assertThat(createdBetSlip).isNotNull()
                              .returns(customerId, BetSlip::getCustomerId)
                              .returns(eventId, BetSlip::getEventId)
                              .returns(eventVersion, BetSlip::getEventVersion)
                              .returns(new BigDecimal("100"), BetSlip::getBetAmount)
                              .returns(5, BetSlip::getBetCount)
                              .returns(BetType.HOME_WIN, BetSlip::getBetType);
    verify(customerService).checkCustomer(customerId);
    verify(eventService).checkEventVersion(eventId, eventVersion);
    verify(repository).save(betSlip);
  }

  @Test
  void createShouldThrowExceptionWhenCustomerCheckThrowsException() {
    Long customerId = 1L;
    BetSlip betSlip = mock(BetSlip.class);
    doThrow(new InvalidCustomerException()).when(customerService).checkCustomer(customerId);

    assertThatThrownBy(() -> betSlipService.create(betSlip, customerId))
        .isInstanceOf(InvalidCustomerException.class)
        .hasMessage("Invalid customer! Please check your customer info.");
    verify(repository, times(0)).save(betSlip);
  }

  @Test
  void createShouldThrowExceptionWhenEventVersionCheckThrowsException() {
    Long customerId = 1L;
    Long eventId = 2L;
    Integer eventVersion = 0;
    BetSlip betSlip = new BetSlip();
    betSlip.setEventId(eventId);
    betSlip.setEventVersion(eventVersion);
    doNothing().when(customerService).checkCustomer(customerId);
    doThrow(new InvalidEventVersionException()).when(eventService).checkEventVersion(eventId, eventVersion);

    assertThatThrownBy(() -> betSlipService.create(betSlip, customerId))
        .isInstanceOf(InvalidEventVersionException.class)
        .hasMessage("The selected event is not current and requires updating!");
    verify(repository, times(0)).save(betSlip);
  }
}
