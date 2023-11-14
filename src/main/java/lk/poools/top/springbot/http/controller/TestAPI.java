package lk.poools.top.springbot.http.controller;

import jakarta.validation.constraints.NotNull;
import lk.poools.top.springbot.dto.PoolDto;
import lk.poools.top.springbot.services.PoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/data")
public class TestAPI {

    //DONE - Get data from date format id{YYYY,MM,DD}, default - today
    //DONE - Get data from range of dates @required {YYYY,MM,DD-YYYY,MM,DD}
    //DONE - Get data from last N-days @required id{days}, validation min 1 - max 300;
    private final PoolService service;

    public TestAPI(PoolService service) {
        this.service = service;
    }

    @RequestMapping("/{date}")
    public ResponseEntity<PoolDto> findByDate(@PathVariable(value = "date") String date)
            throws ParseException {
        log.info("Date accepted: {} ", date);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd");

        try {
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }

        log.debug("Date is valid");
        Optional<PoolDto> pool = service.findDataByDate(date);

        return pool.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping()
    public ResponseEntity<PoolDto> findByDate() {
        return service.findDataForToday().map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping("/range/{range}")
    public ResponseEntity<List<PoolDto>> findByRange(@NotNull @PathVariable(value = "range") String range)
            throws ParseException {

        log.info("Dates accepted : " + range);
        if (range.length() > 21) return ResponseEntity.badRequest().build();

        String[] split = range.split("-");
        if (split.length != 2) return ResponseEntity.badRequest().build();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd");

        LocalDate.parse(split[0], formatter);
        LocalDate.parse(split[1], formatter);

        log.info("Dates are valid");

        List<PoolDto> pools = service.findByRange(split[0], split[1]);

        return ResponseEntity.ok().body(pools);
    }

    @RequestMapping("/days/{days}")
    public ResponseEntity<List<PoolDto>> findByLastDays(@NotNull @PathVariable(value = "days") Integer days) {

        log.info("Days accepted : {} ", days);

        Calendar calendar_today = Calendar.getInstance();
        Calendar calendar_before = (Calendar) calendar_today.clone();

        calendar_before.add(Calendar.DAY_OF_MONTH, -days);

        log.info(calendar_today.toString());
        log.info(calendar_before.toString());

        List<PoolDto> pools = service.findByRange(calendar_before, calendar_today);

        return ResponseEntity.ok().body(pools);
    }
}
