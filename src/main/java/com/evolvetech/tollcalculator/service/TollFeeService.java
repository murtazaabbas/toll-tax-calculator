package com.evolvetech.tollcalculator.service;

import java.time.LocalTime;

public interface TollFeeService {
    int getTollFee(LocalTime time);

    int getTollFee(LocalTime time, String city);
}
