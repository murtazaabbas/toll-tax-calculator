package com.evolvetech.tollcalculator.config;

import com.evolvetech.tollcalculator.model.TollFee;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "toll-fee-data")
@Getter
@Setter
public class TollFeeConfig {
    private Map<String, List<TollFee>> byCity;

    public List<TollFee> getTollFeeByCity(String cityName) {
        return byCity.get(cityName);
    }
}
