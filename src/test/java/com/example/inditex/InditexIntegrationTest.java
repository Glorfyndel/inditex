package com.example.inditex;

import com.example.inditex.application.PriceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {InditexApplication.class, InditexIntegrationTest.TestConfig.class}, properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.h2.console.enabled=true",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.show_sql=true",
        "spring.jpa.properties.hibernate.format_sql=true",
        "spring.liquibase.enabled=true",
        "logging.level.liquibase=DEBUG",
})
public class InditexIntegrationTest {

    @LocalServerPort
    int port;

    RestClient restClient;

    @Configuration
    static class TestConfig {
        // Additional test-specific beans can be defined here if needed

    }

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void test() {
        // Test logic for creating a bank account
        final PriceResponse priceResponse = restClient.get()
                .uri("/inditex/prices?brandId={brandId}&productId={productId}&applicationDate={applicationDate}", 1, 35455, LocalDateTime.parse("2020-06-14T10:00:00"))
                .retrieve()
                .toEntity(PriceResponse.class)
                .getBody();

        assertThat(priceResponse).isNotNull();
    }
}
