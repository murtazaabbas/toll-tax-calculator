package com.evolvetech.tollcalculator.service;

import com.evolvetech.tollcalculator.model.Vehicle;

public interface VehicleService {
    boolean isTollFreeVehicle(Vehicle vehicle);

    Vehicle getVehicle(String type);
}
