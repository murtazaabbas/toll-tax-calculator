package com.evolvetech.tollcalculator.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TollFreeDate {
    private int year;
    private int month;
    private List<Integer> days;
}
