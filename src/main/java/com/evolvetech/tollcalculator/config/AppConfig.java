package com.evolvetech.tollcalculator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app-config")
@Getter
@Setter
public class AppConfig {
    Integer maxFeePerDay;
    Integer allowYear;
    String defaultTollFeeCity;
}
