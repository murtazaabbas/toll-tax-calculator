package com.evolvetech.tollcalculator.service;

import com.evolvetech.tollcalculator.config.VehicleConfig;
import com.evolvetech.tollcalculator.model.Vehicle;
import com.evolvetech.tollcalculator.util.AppConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VehicleServiceTest {

    private final VehicleService vehicleService;

    public VehicleServiceTest() {
        VehicleConfig vehicleConfig = new VehicleConfig();
        Vehicle v1 = new Vehicle();
        v1.setTollFree(true);
        v1.setType("Car");

        Vehicle v2 = new Vehicle();
        v2.setTollFree(true);
        v2.setType("Motorbike");

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(v1);
        vehicles.add(v2);

        vehicleConfig.setVehicles(vehicles);
        vehicleService = new VehicleServiceImpl(vehicleConfig);
    }

    @Test
    @DisplayName("Is Toll Free Vehicle Returns True For Valid Vehicle")
    public void testIsTollFreeVehicleReturnTrueWithValidVehicle() {
        Vehicle vehicle = vehicleService.getVehicle("Motorbike");
        vehicleService.isTollFreeVehicle(vehicle);
        Assertions.assertTrue(vehicleService.isTollFreeVehicle(vehicle));

    }

    @Test
    @DisplayName("Get Vehicle By Valid Type")
    public void testGetVehicleReturnVehicleForValidType() {
        Assertions.assertNotNull(vehicleService.getVehicle("Car"));
    }

    @Test
    @DisplayName("Get Vehicle By Invalid Type Throws Exception")
    public void testGetVehicleThrowsExceptionForInvalidType() {
        RuntimeException re = assertThrows(RuntimeException.class,
                () -> vehicleService.getVehicle("Bike")
        );
        assertEquals(AppConstants.EXCEPTION_INVALID_VEHICLE, re.getMessage());
    }
}
