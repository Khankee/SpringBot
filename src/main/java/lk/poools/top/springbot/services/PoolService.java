package lk.poools.top.springbot.services;

import lk.poools.top.springbot.database.entity.Pool;
import lk.poools.top.springbot.database.repository.PoolRepository;
import lk.poools.top.springbot.dto.PoolDto;
import lk.poools.top.springbot.mapper.PoolMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoolService {

    private final PoolRepository repository;
    private final PoolMapper mapper;

    public void createAndSavePool(String data, Calendar calendar) {
        Pool pool = new Pool();
        pool.setCalendar(calendar);
        pool.setData(data);

        log.info("Saved to db");
        repository.save(pool);
    }

    public boolean isDayBusy(Calendar calendar) {
        return repository.existsPoolByCalendar(calendar);
    }

    public Optional<PoolDto> findDataForToday() {
        Calendar calendar = new GregorianCalendar();
        log.info("Today's date is {} ", calendar);
        return repository
                .findPoolByCalendar(calendar)
                .map(mapper::map);
    }

    public Optional<PoolDto> findDataByDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy,MM,dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(date));
        log.info("Calendar we got is: {} ", calendar);

        return repository
                .findPoolByCalendar(calendar)
                .map(mapper::map);
    }

    public List<PoolDto> findByRange(String start, String end) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy,MM,dd");
        Calendar calendar_begins = Calendar.getInstance();
        Calendar calendar_ends = Calendar.getInstance();

        calendar_begins.setTime(dateFormat.parse(start));
        calendar_ends.setTime(dateFormat.parse(end));

        List<Pool> pools = repository.findAll();

        return pools.stream()
                .filter(val ->
                        val.getCalendar().compareTo(calendar_begins) >= 0 &&
                                val.getCalendar().compareTo(calendar_ends) <= 0)
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public List<PoolDto> findByRange(Calendar start, Calendar end) {
        return repository.findAll()
                .stream()
                .map(mapper::map)
                .filter(val ->
                        val.getCalendar().compareTo(start) >= 0 &&
                        val.getCalendar().compareTo(end) <= 0)
                .toList();
    }
}
