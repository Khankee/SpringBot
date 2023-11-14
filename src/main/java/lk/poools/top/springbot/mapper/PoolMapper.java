package lk.poools.top.springbot.mapper;

import lk.poools.top.springbot.database.entity.Pool;
import lk.poools.top.springbot.dto.PoolDto;
import org.springframework.stereotype.Component;

@Component
public class PoolMapper implements Mapper<Pool, PoolDto> {

    @Override
    public PoolDto map(Pool object) {
        return PoolDto.builder()
                .calendar(object.getCalendar())
                .data(object.getData())
                .build();
    }
}
