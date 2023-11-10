package com.evolvetech.tollcalculator.service;

import com.evolvetech.tollcalculator.config.AppConfig;
import com.evolvetech.tollcalculator.config.TollFreeConfig;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TollFreeDayServiceImpl implements TollFreeDayService {


    private final TollFreeConfig tollFreeConfig;
    private final AppConfig appConfig;
    private Set<LocalDate> tollFreeDates;

    public TollFreeDayServiceImpl(TollFreeConfig tollFreeConfig, AppConfig appConfig) {
        this.tollFreeConfig = tollFreeConfig;
        this.appConfig = appConfig;
        initTollFreeDates();
    }

    private void initTollFreeDates() {
        tollFreeDates = tollFreeConfig.getDates()
                .stream()
                .map(tollFreeDate -> tollFreeDate
                        .getDays()
                        .stream()
                        .map(day -> LocalDate.of(tollFreeDate.getYear(), tollFreeDate.getMonth(), day))
                        .collect(Collectors.toSet()))
                .collect(Collectors.toSet()).stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @Override
    public boolean isTollFreeDate(LocalDate date) {

        if (date == null) {
            return false;
        }
        if (date.getYear() != appConfig.getAllowYear()) {
            return false;
        }
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || tollFreeDates.contains(date);
    }
}
