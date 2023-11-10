package com.evolvetech.tollcalculator.config;

import com.evolvetech.tollcalculator.model.TollFreeDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ConfigurationProperties(prefix = "toll-free-data")
@Getter
@Setter
@NoArgsConstructor
public class TollFreeConfig {
    private List<TollFreeDate> dates;
}
