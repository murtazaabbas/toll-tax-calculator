package com.evolvetech.tollcalculator;

import com.evolvetech.tollcalculator.config.AppConfig;
import com.evolvetech.tollcalculator.config.TollFeeConfig;
import com.evolvetech.tollcalculator.config.TollFreeConfig;
import com.evolvetech.tollcalculator.config.VehicleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AppConfig.class, TollFreeConfig.class, TollFeeConfig.class, VehicleConfig.class})
public class AppRunner {

    public static void main(String[] args) {
        SpringApplication.run(AppRunner.class, args);
    }
}
