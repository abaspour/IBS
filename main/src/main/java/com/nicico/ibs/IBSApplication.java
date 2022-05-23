package com.nicico.ibs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.nicico"})
@EnableJpaAuditing(modifyOnCreate = false, auditorAwareRef = "auditorProvider")
@EntityScan(basePackages = {"com.nicico.ibs.model"})
@EnableJpaRepositories("com.nicico.ibs.repository")
public class IBSApplication {
    public static void main(String[] args) {
        SpringApplication.run(IBSApplication.class, args);
    }
}
