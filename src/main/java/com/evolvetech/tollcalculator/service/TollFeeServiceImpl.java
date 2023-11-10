package com.evolvetech.tollcalculator.service;

import com.evolvetech.tollcalculator.config.AppConfig;
import com.evolvetech.tollcalculator.config.TollFeeConfig;
import com.evolvetech.tollcalculator.model.TollFee;
import com.evolvetech.tollcalculator.util.AppConstants;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TollFeeServiceImpl implements TollFeeService {

    private final TollFeeConfig tollFeeConfig;
    private final AppConfig appConfig;

    public TollFeeServiceImpl(TollFeeConfig tollFeeConfig, AppConfig appConfig) {
        this.tollFeeConfig = tollFeeConfig;
        this.appConfig = appConfig;
    }

    @Override
    public int getTollFee(LocalTime time) {
        return getTollFee(time, null);
    }

    @Override
    public int getTollFee(LocalTime time, String city) {
        if (time == null) {
            return 0;
        }

        if (city == null || city.isBlank()) {
            city = appConfig.getDefaultTollFeeCity();
        }

        Optional<TollFee> fee = getTollFee(city).stream()
                .filter(tollFee -> time.getHour() >= tollFee.getFromHour() && time.getHour() <= tollFee.getToHour() && time.getMinute() >= tollFee.getFromMinute() && time.getMinute() <= tollFee.getToMinute())
                .findFirst();
        if (fee.isPresent()) {
            return fee.get().getFee();
        }
        return 0;
    }

    private List<TollFee> getTollFee(String city) {
        List<TollFee> tollFeeByCity = tollFeeConfig.getTollFeeByCity(city);
        if (tollFeeByCity == null) {
            throw new RuntimeException(AppConstants.EXCEPTION_CITY_DATA_NOT_AVAILABLE);
        }
        return tollFeeByCity;
    }
}
