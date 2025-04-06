package com.berkaydemirel.betslip.dto;

import com.berkaydemirel.betslip.data.BetSlip;
import org.mapstruct.Mapper;

/**
 * @author berkaydemirel
 */
@Mapper(componentModel = "spring")
public interface BetSlipMapper {

  BetSlipDTO toDto(BetSlip betSlip);

  BetSlip toEntity(BetSlipDTO betSlipDTO);
}
