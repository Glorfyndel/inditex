package com.example.inditex;

import com.example.inditex.application.PriceResponse;
import com.example.inditex.domain.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @DisplayName("Consultar precio de producto '1-35455' :")
    @ParameterizedTest(name = "A las {2} -> tarifa {3}")
    @CsvSource(delimiter = '|', value = {
            /* brandId | productId | applicationDate     | priceList | startDate           | endDate             | price  | currency */
            "  1       | 35455     | 2020-06-14T10:00:00 | 1         | 2020-06-14T00:00:00 | 2020-12-31T23:59:59 | 35.50  | EUR      ",
            "  1       | 35455     | 2020-06-14T16:00:00 | 2         | 2020-06-14T15:00:00 | 2020-06-14T18:30:00 | 25.45  | EUR      ",
            "  1       | 35455     | 2020-06-14T21:00:00 | 1         | 2020-06-14T00:00:00 | 2020-12-31T23:59:59 | 35.50  | EUR      ",
            "  1       | 35455     | 2020-06-15T10:00:00 | 3         | 2020-06-15T00:00:00 | 2020-06-15T11:00:00 | 30.50  | EUR      ",
            "  1       | 35455     | 2020-06-16T21:00:00 | 4         | 2020-06-15T16:00:00 | 2020-12-31T23:59:59 | 38.95  | EUR      "
    })
    void consultarPrecioProducto(long brandId, long productId, String applicationDate, int priceList, String startDate,
                                 String endDate, String price, String currency) {

        final PriceResponse priceResponse = restClient.get()
                .uri("/inditex/prices?brandId={brandId}&productId={productId}&applicationDate={applicationDate}",
                        brandId, productId, LocalDateTime.parse(applicationDate))
                .retrieve()
                .toEntity(PriceResponse.class)
                .getBody();

        assertThat(priceResponse)
                .isEqualTo(PriceResponse.builder()
                        .brandId(brandId)
                        .productId(productId)
                        .priceList(priceList)
                        .startDate(LocalDateTime.parse(startDate))
                        .endDate(LocalDateTime.parse(endDate))
                        .price(new BigDecimal(price))
                        .currency(Currency.valueOf(currency))
                        .build());
    }
}
