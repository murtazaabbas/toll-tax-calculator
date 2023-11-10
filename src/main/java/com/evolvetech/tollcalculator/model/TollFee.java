package com.evolvetech.tollcalculator.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TollFee {
    private int fromHour;
    private int toHour;
    private int fromMinute;
    private int toMinute;
    private int fee;
}
