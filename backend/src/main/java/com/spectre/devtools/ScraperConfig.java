package com.spectre.devtools;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication 
@ComponentScan(basePackages = {
        "com.spectre.payload.tools",
        "com.spectre.repository"
})
@EnableJpaRepositories(basePackages = "com.spectre.repository")
@EntityScan(basePackages = "com.spectre.model")
public class ScraperConfig {
}
