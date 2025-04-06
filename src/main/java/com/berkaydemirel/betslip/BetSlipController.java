package com.berkaydemirel.betslip;

import com.berkaydemirel.betslip.dto.BetSlipDTO;
import com.berkaydemirel.betslip.dto.BetSlipMapper;
import com.berkaydemirel.betslip.service.BetSlipService;
import com.berkaydemirel.event.exception.EventNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author berkaydemirel
 */
@RestController
@RequestMapping("/api/v1/betslips")
public class BetSlipController {

  private final BetSlipService service;

  private final BetSlipMapper mapper;

  public BetSlipController(BetSlipService service, BetSlipMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<BetSlipDTO> create(@RequestHeader("customer-id") Long customerId,
                                           @RequestBody @Valid BetSlipDTO betSlip)
      throws EventNotFoundException, InterruptedException {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(mapper.toDto(service.create(mapper.toEntity(betSlip), customerId)));
  }
}
