package com.evolvetech.tollcalculator.service;

import com.evolvetech.tollcalculator.config.VehicleConfig;
import com.evolvetech.tollcalculator.model.Vehicle;
import com.evolvetech.tollcalculator.util.AppConstants;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleConfig vehicleConfig;

    public VehicleServiceImpl(VehicleConfig vehicleConfig) {
        this.vehicleConfig = vehicleConfig;
    }

    @Override
    public boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null) return false;
        String vehicleType = vehicle.getType();
        Optional<Vehicle> tollFreeVehicle = getVehicles().stream()
                .filter(v -> v.getType().equals(vehicleType) && v.isTollFree()).findFirst();
        return tollFreeVehicle.isPresent();
    }

    @Override
    public Vehicle getVehicle(String type) {
        return getVehicles().stream()
                .filter(v -> v.getType().equalsIgnoreCase(type)).findAny().orElseThrow(() -> new RuntimeException(AppConstants.EXCEPTION_INVALID_VEHICLE));
    }


    private List<Vehicle> getVehicles() {
        return vehicleConfig.getVehicles();
    }
}
