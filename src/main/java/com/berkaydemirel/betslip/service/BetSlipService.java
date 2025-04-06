package com.berkaydemirel.betslip.service;

import com.berkaydemirel.betslip.data.BetSlip;
import com.berkaydemirel.betslip.data.BetSlipRepository;
import com.berkaydemirel.customer.service.CustomerService;
import com.berkaydemirel.event.exception.InvalidEventVersionException;
import com.berkaydemirel.event.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author berkaydemirel
 */
@Service
public class BetSlipService {

  private final BetSlipRepository repository;

  private final EventService eventService;

  private final CustomerService customerService;

  public BetSlipService(BetSlipRepository repository, EventService eventService, CustomerService customerService) {
    this.repository = repository;
    this.eventService = eventService;
    this.customerService = customerService;
  }

  @Transactional
  public BetSlip create(BetSlip betSlip, Long customerId) throws InvalidEventVersionException, InterruptedException {
    customerService.checkCustomer(customerId);
    betSlip.setCustomerId(customerId);
    eventService.checkEventVersion(betSlip.getEventId(), betSlip.getEventVersion());
    return repository.save(betSlip);
  }
}