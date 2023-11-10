package com.evolvetech.tollcalculator.service;

import com.evolvetech.tollcalculator.config.AppConfig;
import com.evolvetech.tollcalculator.config.TollFeeConfig;
import com.evolvetech.tollcalculator.model.TollFee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TollFeeServiceTest {

    private final TollFeeService tollFeeService;

    public TollFeeServiceTest() {
        AppConfig appConfig = new AppConfig();
        appConfig.setAllowYear(2013);
        appConfig.setMaxFeePerDay(60);
        appConfig.setDefaultTollFeeCity("gothenburg");

        TollFeeConfig tollFeeConfig = new TollFeeConfig();
        TollFee t1 = new TollFee();
        t1.setFee(12);
        t1.setFromHour(6);
        t1.setFromMinute(0);
        t1.setToHour(6);
        t1.setToMinute(29);

        TollFee t2 = new TollFee();
        t2.setFee(10);
        t2.setFromHour(14);
        t2.setFromMinute(0);
        t2.setToHour(14);
        t2.setToMinute(59);

        List<TollFee> tollFees = new ArrayList<>();
        tollFees.add(t1);
        tollFees.add(t2);

        Map<String, List<TollFee>> tollFeeMap = new HashMap<>();
        tollFeeMap.put("gothenburg", tollFees);
        tollFeeConfig.setByCity(tollFeeMap);

        tollFeeService = new TollFeeServiceImpl(tollFeeConfig, appConfig);
    }

    @Test
    @DisplayName("Return Fee for Valid Time Having Fee")
    public void testGetTollFeeReturnFeeForValidTime() {
        assertEquals(12, tollFeeService.getTollFee(LocalTime.of(6, 0)));

    }

    @Test
    @DisplayName("Return Zero Fee For Null Time")
    public void testGetTollFeeReturnZeroForValidTime() {
        assertEquals(0, tollFeeService.getTollFee(null));
    }

    @Test
    @DisplayName("Return Zero for Valid Time Having No Fee")
    public void testGetTollFeeReturnZeroForValidTimeHavingNoFee() {
        assertEquals(0, tollFeeService.getTollFee(LocalTime.of(4, 0)));

    }
}
