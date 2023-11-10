package com.evolvetech.tollcalculator.service;

import java.time.LocalDate;

public interface TollFreeDayService {
    public boolean isTollFreeDate(LocalDate date);
}
