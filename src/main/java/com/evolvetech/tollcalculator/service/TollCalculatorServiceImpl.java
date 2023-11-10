package com.evolvetech.tollcalculator.service;

import com.evolvetech.tollcalculator.config.AppConfig;
import com.evolvetech.tollcalculator.model.TollTaxCalculatorRequest;
import com.evolvetech.tollcalculator.model.TollTaxCalculatorResponse;
import com.evolvetech.tollcalculator.model.Vehicle;
import com.evolvetech.tollcalculator.util.AppConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TollCalculatorServiceImpl implements TollCalculatorService {

    private final TollFeeService tollFeeService;
    private final TollFreeDayService tollFreeDayService;
    private final VehicleService vehicleService;
    private final AppConfig appConfig;

    public TollCalculatorServiceImpl(TollFeeService tollFeeService, TollFreeDayService tollFreeDayService,
                                     VehicleService vehicleService, AppConfig appConfig) {
        this.tollFeeService = tollFeeService;
        this.tollFreeDayService = tollFreeDayService;
        this.vehicleService = vehicleService;
        this.appConfig = appConfig;
    }

    @Override
    public TollTaxCalculatorResponse getCalculatedTollTax(TollTaxCalculatorRequest taxCalculatorRequest) {
        TollTaxCalculatorResponse response = new TollTaxCalculatorResponse();

        Vehicle vehicle = vehicleService.getVehicle(taxCalculatorRequest.getVehicle());
        List<LocalDateTime> tollDateTimeList = taxCalculatorRequest.getTollDateTime();
        if(tollDateTimeList == null || tollDateTimeList.isEmpty()){
            throw new RuntimeException(AppConstants.EXCEPTION_NULL_DATETIME);
        }

        Collections.sort(tollDateTimeList);
        // multiple dates and time slots
        Map<LocalDate, List<LocalDateTime>> dateToDateTimeMap = tollDateTimeList.stream()
                .collect(Collectors.groupingBy(localDateTime -> localDateTime.toLocalDate()));

        response.setFee(getTollFee(vehicle, taxCalculatorRequest.getCity(), dateToDateTimeMap));
        return response;
    }

    private int getTollFee(Vehicle vehicle, String city, Map<LocalDate, List<LocalDateTime>> dates) {
        int totalFee = 0;
        for (Map.Entry<LocalDate, List<LocalDateTime>> entry : dates.entrySet()) {
            if (validateTollFeeParams(vehicle, entry.getValue())) {
                continue;
            }

            List<LocalTime> validTimeList = getValidFeeTimeList(city, entry.getValue());
            if (validTimeList.isEmpty()) {
                continue;
            }

            totalFee += calculateFee(validTimeList, city);
        }
        return totalFee;
    }

    private boolean validateTollFeeParams(Vehicle vehicle, List<LocalDateTime> dates) {
        if (vehicle == null) {
            throw new RuntimeException(AppConstants.EXCEPTION_NULL_VEHICLE);
        }
        if (dates == null || dates.size() == 0) {
            throw new RuntimeException(AppConstants.EXCEPTION_NULL_DATETIME);
        }
        if (vehicleService.getVehicle(vehicle.getType()) == null) {
            throw new RuntimeException(AppConstants.EXCEPTION_INVALID_VEHICLE);
        }

        if(vehicleService.isTollFreeVehicle(vehicle)){
            return true;
        }

        return tollFreeDayService.isTollFreeDate(dates.get(0).toLocalDate());
    }

    private List<LocalTime> getValidFeeTimeList(String city, List<LocalDateTime> dates) {
        return dates.stream()
                .map(LocalDateTime::toLocalTime)
                .filter(time -> tollFeeService.getTollFee(time, city) > 0)
                .sorted()
                .collect(Collectors.toList());
    }

    private int calculateFee(List<LocalTime> timeList, String city) {
        LocalTime intervalStart = timeList.get(0);
        int totalFee = 0;
        Set<Integer> intervalFees = new HashSet<>();
        for (LocalTime date : timeList) {
            if (ChronoUnit.MINUTES.between(intervalStart, date) > 60) {
                intervalStart = date;
                totalFee += Collections.max(intervalFees);
                intervalFees.clear();
            }
            intervalFees.add(tollFeeService.getTollFee(date, city));
        }
        if (!intervalFees.isEmpty()) {
            totalFee += Collections.max(intervalFees);
        }
        return Math.min(totalFee, appConfig.getMaxFeePerDay());
    }
}
