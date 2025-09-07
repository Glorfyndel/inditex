package com.example.inditex;

import com.example.inditex.application.PriceResponse;
import com.example.inditex.domain.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {InditexApplication.class, InditexIntegrationTest.TestConfig.class}, properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.liquibase.enabled=true",
        "spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml",
        "spring.h2.console.enabled=true",
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.jpa.properties.hibernate.show_sql=false",
        "spring.jpa.properties.hibernate.format_sql=false",
})
public class InditexIntegrationTest {

    @LocalServerPort
    int port;

    RestClient restClient;

    @Configuration
    static class TestConfig {

    }

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    @DisplayName("Consulta el precio para el 14/06/2020 a las 10:00 -> Debe devolver el precio de la tarifa 1")
    void prueba1() {
        final PriceResponse priceResponse = restClient.get()
                .uri("/inditex/prices?brandId={brandId}&productId={productId}&applicationDate={applicationDate}",
                        1, 35455, LocalDateTime.parse("2020-06-14T10:00:00"))
                .retrieve()
                .toEntity(PriceResponse.class)
                .getBody();

        assertThat(priceResponse)
                .isEqualTo(PriceResponse.builder()
                        .brandId(1L)
                        .productId(35455L)
                        .priceList(1)
                        .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                        .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                        .price(new BigDecimal("35.50"))
                        .currency(Currency.EUR)
                        .build());
    }

    @Test
    @DisplayName("Consulta el precio para el 14/06/2020 a las 16:00 -> Debe devolver el precio de la tarifa 2")
    void prueba2() {
        final PriceResponse priceResponse = restClient.get()
                .uri("/inditex/prices?brandId={brandId}&productId={productId}&applicationDate={applicationDate}",
                        1, 35455, LocalDateTime.parse("2020-06-14T16:00:00"))
                .retrieve()
                .toEntity(PriceResponse.class)
                .getBody();

        assertThat(priceResponse)
                .isEqualTo(PriceResponse.builder()
                        .brandId(1L)
                        .productId(35455L)
                        .priceList(2)
                        .startDate(LocalDateTime.parse("2020-06-14T15:00:00"))
                        .endDate(LocalDateTime.parse("2020-06-14T18:30:00"))
                        .price(new BigDecimal("25.45"))
                        .currency(Currency.EUR)
                        .build());
    }

    @Test
    @DisplayName("Consulta el precio para el 14/06/2020 a las 21:00 -> Debe devolver el precio de la tarifa 1")
    void prueba3() {
        final PriceResponse priceResponse = restClient.get()
                .uri("/inditex/prices?brandId={brandId}&productId={productId}&applicationDate={applicationDate}",
                        1, 35455, LocalDateTime.parse("2020-06-14T21:00:00"))
                .retrieve()
                .toEntity(PriceResponse.class)
                .getBody();

        assertThat(priceResponse)
                .isEqualTo(PriceResponse.builder()
                        .brandId(1L)
                        .productId(35455L)
                        .priceList(1)
                        .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                        .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                        .price(new BigDecimal("35.50"))
                        .currency(Currency.EUR)
                        .build());
    }

    @Test
    @DisplayName("Consulta el precio para el 15/06/2020 a las 10:00 -> Debe devolver el precio de la tarifa 3")
    void prueba4() {
        final PriceResponse priceResponse = restClient.get()
                .uri("/inditex/prices?brandId={brandId}&productId={productId}&applicationDate={applicationDate}",
                        1, 35455, LocalDateTime.parse("2020-06-15T10:00:00"))
                .retrieve()
                .toEntity(PriceResponse.class)
                .getBody();

        assertThat(priceResponse)
                .isEqualTo(PriceResponse.builder()
                        .brandId(1L)
                        .productId(35455L)
                        .priceList(3)
                        .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
                        .endDate(LocalDateTime.parse("2020-06-15T11:00:00"))
                        .price(new BigDecimal("30.50"))
                        .currency(Currency.EUR)
                        .build());
    }

    @Test
    @DisplayName("Consulta el precio para el 16/06/2020 a las 21:00 -> Debe devolver el precio de la tarifa 4")
    void prueba5() {
        final PriceResponse priceResponse = restClient.get()
                .uri("/inditex/prices?brandId={brandId}&productId={productId}&applicationDate={applicationDate}",
                        1, 35455, LocalDateTime.parse("2020-06-16T21:00:00"))
                .retrieve()
                .toEntity(PriceResponse.class)
                .getBody();

        assertThat(priceResponse)
                .isEqualTo(PriceResponse.builder()
                        .brandId(1L)
                        .productId(35455L)
                        .priceList(4)
                        .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
                        .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                        .price(new BigDecimal("38.95"))
                        .currency(Currency.EUR)
                        .build());
    }
}
