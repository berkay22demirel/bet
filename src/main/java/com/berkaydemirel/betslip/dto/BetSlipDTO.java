package com.berkaydemirel.betslip.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * @author berkaydemirel
 */
public record BetSlipDTO(@JsonProperty(access = JsonProperty.Access.READ_ONLY) Long id,
                         @JsonProperty(access = JsonProperty.Access.READ_ONLY) Long customerId,
                         @NotNull Long eventId,
                         @NotNull Integer eventVersion,
                         @NotNull BetType betType,
                         @NotNull @Positive @Max(500) Integer betCount,
                         @NotNull @DecimalMin(value = "0.0", inclusive = false)
                         @DecimalMax(value = "10000.0") BigDecimal betAmount) {

}
