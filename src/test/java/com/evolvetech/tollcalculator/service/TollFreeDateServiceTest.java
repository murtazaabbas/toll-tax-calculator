package com.evolvetech.tollcalculator.service;

import com.evolvetech.tollcalculator.config.AppConfig;
import com.evolvetech.tollcalculator.config.TollFreeConfig;
import com.evolvetech.tollcalculator.model.TollFreeDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TollFreeDateServiceTest {

    private TollFreeDayService tollFreeDayService;

    public TollFreeDateServiceTest() {
        AppConfig appConfig = new AppConfig();
        appConfig.setAllowYear(2013);
        appConfig.setMaxFeePerDay(60);
        appConfig.setDefaultTollFeeCity("gothenburg");

        TollFreeConfig tollFreeConfig = new TollFreeConfig();
        TollFreeDate t1 = new TollFreeDate();
        t1.setYear(2013);
        t1.setMonth(2);
        t1.setDays(List.of(1,2,3,5,8));

        TollFreeDate t2 = new TollFreeDate();
        t2.setYear(2013);
        t2.setMonth(6);
        t2.setDays(List.of(10,11,22,29));

        List<TollFreeDate> tollFreeDates = new ArrayList<>();
        tollFreeDates.add(t1);
        tollFreeDates.add(t2);
        tollFreeConfig.setDates(tollFreeDates);
        tollFreeDayService = new TollFreeDayServiceImpl(tollFreeConfig, appConfig);
    }

    @Test
    @DisplayName("Free Date True For Validate Date")
    public void TestsTollFreeDateReturnsTrueForValidDate() {
        assertTrue(tollFreeDayService.isTollFreeDate(LocalDate.parse("2013-02-05")));

    }

    @Test
    @DisplayName("Free Date False For null Date")
    public void TestsTollFreeDateReturnsFalseForNull() {
        assertFalse(tollFreeDayService.isTollFreeDate(null));

    }

    @Test
    @DisplayName("Free Date False For none free fee Date")
    public void TestsTollFreeDateReturnsFalseForNoneFreeDate() {
        assertFalse(tollFreeDayService.isTollFreeDate(LocalDate.parse("2013-06-12")));
    }
}
