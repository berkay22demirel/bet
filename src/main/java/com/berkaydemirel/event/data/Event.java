package com.berkaydemirel.event.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author berkaydemirel
 */
@Entity
public class Event {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String leagueName;

  @Column(nullable = false)
  private String homeTeamName;

  @Column(nullable = false)
  private String awayTeamName;

  @Column(nullable = false)
  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal homeWinsRate;

  @Column(nullable = false)
  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal awayWinsRate;

  @Column(nullable = false)
  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal drawRate;

  @Column(columnDefinition = "TIMESTAMP", nullable = false)
  private LocalDateTime eventDate;

  @Version
  private Integer version;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLeagueName() {
    return leagueName;
  }

  public void setLeagueName(String leagueName) {
    this.leagueName = leagueName;
  }

  public String getHomeTeamName() {
    return homeTeamName;
  }

  public void setHomeTeamName(String homeTeamName) {
    this.homeTeamName = homeTeamName;
  }

  public String getAwayTeamName() {
    return awayTeamName;
  }

  public void setAwayTeamName(String awayTeamName) {
    this.awayTeamName = awayTeamName;
  }

  public BigDecimal getHomeWinsRate() {
    return homeWinsRate;
  }

  public void setHomeWinsRate(BigDecimal homeWinsRate) {
    this.homeWinsRate = homeWinsRate;
  }

  public BigDecimal getAwayWinsRate() {
    return awayWinsRate;
  }

  public void setAwayWinsRate(BigDecimal awayWinsRate) {
    this.awayWinsRate = awayWinsRate;
  }

  public BigDecimal getDrawRate() {
    return drawRate;
  }

  public void setDrawRate(BigDecimal drawRate) {
    this.drawRate = drawRate;
  }

  public LocalDateTime getEventDate() {
    return eventDate;
  }

  public void setEventDate(LocalDateTime eventDate) {
    this.eventDate = eventDate;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}
