package com.berkaydemirel.betslip.data;

import com.berkaydemirel.betslip.dto.BetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import java.math.BigDecimal;

/**
 * @author berkaydemirel
 */
@Entity
public class BetSlip {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private Long eventId;

  @Column(nullable = false)
  private Integer eventVersion;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BetType betType;

  @Column(nullable = false)
  private Long customerId;

  @Column(nullable = false)
  @Max(500)
  private Integer betCount;

  @Column(nullable = false)
  @DecimalMin(value = "0.0", inclusive = false)
  @DecimalMax(value = "10000.00")
  private BigDecimal betAmount;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  public Integer getEventVersion() {
    return eventVersion;
  }

  public void setEventVersion(Integer eventVersion) {
    this.eventVersion = eventVersion;
  }

  public BetType getBetType() {
    return betType;
  }

  public void setBetType(BetType betType) {
    this.betType = betType;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public Integer getBetCount() {
    return betCount;
  }

  public void setBetCount(Integer betCount) {
    this.betCount = betCount;
  }

  public BigDecimal getBetAmount() {
    return betAmount;
  }

  public void setBetAmount(BigDecimal betAmount) {
    this.betAmount = betAmount;
  }
}
