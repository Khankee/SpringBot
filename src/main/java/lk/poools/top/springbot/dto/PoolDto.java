package lk.poools.top.springbot.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Calendar;

@Value
@Builder
public class PoolDto {
    Calendar calendar;
    String data;
}
