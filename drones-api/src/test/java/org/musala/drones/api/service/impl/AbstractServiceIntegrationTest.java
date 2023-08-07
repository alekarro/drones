package org.musala.drones.api.service.impl;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractServiceIntegrationTest {
    static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("dronestestdb")
            .withUsername("tester")
            .withPassword("password");

    static {
        postgresqlContainer.start();
    }
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }
}
