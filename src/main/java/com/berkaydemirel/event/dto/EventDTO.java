package com.berkaydemirel.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author berkaydemirel
 */
public record EventDTO(@JsonProperty(access = JsonProperty.Access.READ_ONLY) Long id,
                       @JsonProperty(access = JsonProperty.Access.READ_ONLY) Integer version,
                       @NotBlank @Size(max = 255) String leagueName,
                       @NotBlank @Size(max = 255) String homeTeamName,
                       @NotBlank @Size(max = 255) String awayTeamName,
                       @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal homeWinsRate,
                       @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal awayWinsRate,
                       @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal drawRate,
                       @NotNull LocalDateTime eventDate) {

}
