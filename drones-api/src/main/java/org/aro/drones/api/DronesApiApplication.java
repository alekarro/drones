package org.aro.drones.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DronesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DronesApiApplication.class, args);
    }

}
