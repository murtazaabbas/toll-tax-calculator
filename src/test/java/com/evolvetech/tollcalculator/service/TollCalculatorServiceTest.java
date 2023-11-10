package com.evolvetech.tollcalculator.service;

import com.evolvetech.tollcalculator.config.AppConfig;
import com.evolvetech.tollcalculator.config.TollFeeConfig;
import com.evolvetech.tollcalculator.config.TollFreeConfig;
import com.evolvetech.tollcalculator.config.VehicleConfig;
import com.evolvetech.tollcalculator.model.*;
import com.evolvetech.tollcalculator.service.*;
import com.evolvetech.tollcalculator.util.AppConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TollCalculatorServiceTest {

    private final TollCalculatorService tollCalculatorService;
    private final VehicleServiceImpl vehicleService;
    private final TollFeeService tollFeeService;
    private final TollFreeDayService tollFreeDayService;
    private final AppConfig appConfig;

    public TollCalculatorServiceTest() {
        VehicleConfig vehicleConfig = new VehicleConfig();
        Vehicle v1 = new Vehicle();
        v1.setTollFree(false);
        v1.setType("Car");

        Vehicle v2 = new Vehicle();
        v2.setTollFree(true);
        v2.setType("Motorbike");

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(v1);
        vehicles.add(v2);

        vehicleConfig.setVehicles(vehicles);
        this.vehicleService = new VehicleServiceImpl(vehicleConfig);

        appConfig = new AppConfig();
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

        TollFee t3 = new TollFee();
        t3.setFee(40);
        t3.setFromHour(16);
        t3.setFromMinute(0);
        t3.setToHour(16);
        t3.setToMinute(59);

        TollFee t4 = new TollFee();
        t4.setFee(40);
        t4.setFromHour(17);
        t4.setFromMinute(0);
        t4.setToHour(17);
        t4.setToMinute(59);

        List<TollFee> tollFees = new ArrayList<>();
        tollFees.add(t1);
        tollFees.add(t2);
        tollFees.add(t3);
        tollFees.add(t4);

        Map<String, List<TollFee>> tollFeeMap = new HashMap<>();
        tollFeeMap.put("gothenburg", tollFees);
        tollFeeConfig.setByCity(tollFeeMap);

        tollFeeService = new TollFeeServiceImpl(tollFeeConfig, appConfig);

        TollFreeConfig tollFreeConfig = new TollFreeConfig();
        TollFreeDate tf1 = new TollFreeDate();
        tf1.setYear(2013);
        tf1.setMonth(2);
        tf1.setDays(List.of(1,2,3,5,8));

        TollFreeDate tf2 = new TollFreeDate();
        tf2.setYear(2013);
        tf2.setMonth(6);
        tf2.setDays(List.of(10,11,22,29));

        List<TollFreeDate> tollFreeDates = new ArrayList<>();
        tollFreeDates.add(tf1);
        tollFreeDates.add(tf2);
        tollFreeConfig.setDates(tollFreeDates);
        tollFreeDayService = new TollFreeDayServiceImpl(tollFreeConfig, appConfig);

        tollCalculatorService = new TollCalculatorServiceImpl(tollFeeService, tollFreeDayService,
                vehicleService, appConfig);
    }

    @Test
    @DisplayName("Null Vehicle Throws Exception")
    public void testGetTollFeeWithNullVehicleThrowsException() {
        TollTaxCalculatorRequest taxCalculatorRequest = new TollTaxCalculatorRequest();
        taxCalculatorRequest.setVehicle(null);
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> tollCalculatorService.getCalculatedTollTax(taxCalculatorRequest));
        assertEquals(AppConstants.EXCEPTION_INVALID_VEHICLE, runtimeException.getMessage());
    }

    @Test
    @DisplayName("Invalid Vehicle Throws Exception")
    public void testGetTollFeeWithInvalidVehicleThrowsException() {
        TollTaxCalculatorRequest taxCalculatorRequest = new TollTaxCalculatorRequest();
        taxCalculatorRequest.setVehicle("tricycle");
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> tollCalculatorService.getCalculatedTollTax(taxCalculatorRequest));
        assertEquals(AppConstants.EXCEPTION_INVALID_VEHICLE, runtimeException.getMessage());
    }

    @Test
    @DisplayName("Null Datetime Throws Exception")
    public void testGetTollFeeWithValidVehicleAndNullDateThrowsException() {
        TollTaxCalculatorRequest taxCalculatorRequest = new TollTaxCalculatorRequest();
        taxCalculatorRequest.setVehicle("Car");
        taxCalculatorRequest.setCity("gothenburg");
        RuntimeException re = assertThrows(RuntimeException.class,
                () -> tollCalculatorService.getCalculatedTollTax(taxCalculatorRequest));
        assertEquals(AppConstants.EXCEPTION_NULL_DATETIME, re.getMessage());
    }

    @Test
    @DisplayName("Toll Free Dates")
    public void testGetTollFeeWithValidVehicle() {
        TollTaxCalculatorRequest taxCalculatorRequest = new TollTaxCalculatorRequest();
        taxCalculatorRequest.setVehicle("Motorbike");
        taxCalculatorRequest.setCity("gothenburg");
        taxCalculatorRequest.setTollDateTime(
                Arrays.asList(
                        LocalDateTime.of(2013, 8, 28, 14, 0),
                        LocalDateTime.of(2013, 8, 28, 06, 0))
        );
        TollTaxCalculatorResponse calculatedTollTax = tollCalculatorService.getCalculatedTollTax(taxCalculatorRequest);
        assertEquals(0, calculatedTollTax.getFee());
    }

    @Test
    @DisplayName("Toll Free Single Date And Multiple times")
    public void testGetTollFeeForSingleDateAndMultipleTimes() {
        TollTaxCalculatorRequest taxCalculatorRequest = new TollTaxCalculatorRequest();
        taxCalculatorRequest.setVehicle("Car");
        taxCalculatorRequest.setCity("gothenburg");
        taxCalculatorRequest.setTollDateTime(Arrays.asList(LocalDateTime.of(2013, 8, 07, 06, 0),
                LocalDateTime.of(2013, 8, 07, 14, 15)));

        TollTaxCalculatorResponse calculatedTollTax = tollCalculatorService.getCalculatedTollTax(taxCalculatorRequest);
        assertEquals(22, calculatedTollTax.getFee());
    }

    @Test
    @DisplayName("Toll Free Multiple Dates And Multiple times")
    public void testGetTollFeeForMultipleDatesAndMultipleTimes() {
        TollTaxCalculatorRequest taxCalculatorRequest = new TollTaxCalculatorRequest();
        taxCalculatorRequest.setVehicle("Car");
        taxCalculatorRequest.setCity("gothenburg");
        taxCalculatorRequest.setTollDateTime(
                Arrays.asList(
                        LocalDateTime.of(2013, 8, 7, 06, 0),
                        LocalDateTime.of(2013, 8, 7, 14, 15),
                        LocalDateTime.of(2013, 4, 8, 06, 0),
                        LocalDateTime.of(2013, 4, 8, 14, 15)
                ));

        TollTaxCalculatorResponse calculatedTollTax = tollCalculatorService.getCalculatedTollTax(taxCalculatorRequest);
        assertEquals(44, calculatedTollTax.getFee());
    }

    @Test
    @DisplayName("Toll Free Charge Once A Hour And Highest Value Will Be Applied")
    public void testGetTollFeeWithValidVehicleAndTollFeeRushHours() {
        TollTaxCalculatorRequest taxCalculatorRequest = new TollTaxCalculatorRequest();
        taxCalculatorRequest.setVehicle("Car");
        taxCalculatorRequest.setCity("gothenburg");
        taxCalculatorRequest.setTollDateTime(
                Arrays.asList(
                        LocalDateTime.of(2013, 8, 7, 06, 0),
                        LocalDateTime.of(2013, 8, 7, 06, 15),
                        LocalDateTime.of(2013, 4, 7, 06, 20),
                        LocalDateTime.of(2013, 4, 7, 06, 25)
                ));

        TollTaxCalculatorResponse calculatedTollTax = tollCalculatorService.getCalculatedTollTax(taxCalculatorRequest);
        assertEquals(12, calculatedTollTax.getFee());
    }
    @Test
    @DisplayName("Max 60 Toll Fee For One Day for valid vehicle")
    public void testGetTollFeeWithValidVehicleMaxFeeOneDay() {
        TollTaxCalculatorRequest taxCalculatorRequest = new TollTaxCalculatorRequest();
        taxCalculatorRequest.setVehicle("Car");
        taxCalculatorRequest.setCity("gothenburg");
        taxCalculatorRequest.setTollDateTime(
                Arrays.asList(
                        LocalDateTime.of(2013, 4, 8, 16, 0),
                        LocalDateTime.of(2013, 4, 8, 17, 15)
                ));
        TollTaxCalculatorResponse calculatedTollTax = tollCalculatorService.getCalculatedTollTax(taxCalculatorRequest);
        assertEquals(60, calculatedTollTax.getFee());
    }
}
