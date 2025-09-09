package com.example.inditex.infrastructure;

import com.example.inditex.domain.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false, properties = {
        "spring.jpa.properties.hibernate.format_sql=false",
        "spring.liquibase.enabled=false",
})
class PriceEntityRepositoryTest {
    @Autowired
    private PriceEntityRepository priceEntityRepository;

    @DisplayName("Find current price when different priority -> chose the highest priority")
    @Test
    void testFinPriceWhenDifferentPriority() {
        // GIVEN
        final List<PriceEntity> entities = List.of(
                PriceEntity.builder()
                        .id(PriceEntity.Id.builder()
                                .brandId(1L)
                                .productId(35455L)
                                .priceList(1)
                                .build())
                        .startDate("2020-06-14-00.00.00")
                        .endDate("2020-12-31-23.59.59")
                        .priority(0)
                        .price(new BigDecimal("35.50"))
                        .currency(Currency.EUR)
                        .build(),
                PriceEntity.builder()
                        .id(PriceEntity.Id.builder()
                                .brandId(1L)
                                .productId(35455L)
                                .priceList(2)
                                .build())
                        .startDate("2020-06-14-15.00.00")
                        .endDate("2020-06-14-18.30.00")
                        .priority(1)
                        .price(new BigDecimal("25.45"))
                        .currency(Currency.EUR)
                        .build());

        priceEntityRepository.saveAllAndFlush(entities);

        // WHEN
        final Optional<PriceEntity> optionalCurrentPrice = priceEntityRepository.findCurrentPrice(1L, 35455L, "2020-06-14-10.00.00");

        // THEN
        assertThat(optionalCurrentPrice)
                .isPresent()
                .hasValue(PriceEntity.builder()
                        .id(PriceEntity.Id.builder()
                                .brandId(1L)
                                .productId(35455L)
                                .priceList(1)
                                .build())
                        .startDate("2020-06-14-00.00.00")
                        .endDate("2020-12-31-23.59.59")
                        .priority(0)
                        .price(new BigDecimal("35.50"))
                        .currency(Currency.EUR)
                        .build());
    }

    @DisplayName("Find current price when equal priority -> chose the last one, highest 'priceList'")
    @Test
    void testFindPriceWhenEqualPriority() {
        // GIVEN
        final List<PriceEntity> entities = List.of(
                PriceEntity.builder()
                        .id(PriceEntity.Id.builder()
                                .brandId(1L)
                                .productId(35455L)
                                .priceList(1)
                                .build())
                        .startDate("2020-06-14-00.00.00")
                        .endDate("2020-12-31-23.59.59")
                        .priority(0)
                        .price(new BigDecimal("35.50"))
                        .currency(Currency.EUR)
                        .build(),
                PriceEntity.builder()
                        .id(PriceEntity.Id.builder()
                                .brandId(1L)
                                .productId(35455L)
                                .priceList(2)
                                .build())
                        .startDate("2020-06-14-00.00.00")
                        .endDate("2020-12-31-23.59.59")
                        .priority(0)
                        .price(new BigDecimal("33.33"))
                        .currency(Currency.EUR)
                        .build()
        );

        priceEntityRepository.saveAllAndFlush(entities);

        // WHEN
        final Optional<PriceEntity> optionalCurrentPrice = priceEntityRepository.findCurrentPrice(1L, 35455L, "2020-06-14-16.00.00");

        // THEN
        assertThat(optionalCurrentPrice)
                .isPresent()
                .hasValue(PriceEntity.builder()
                        .id(PriceEntity.Id.builder()
                                .brandId(1L)
                                .productId(35455L)
                                .priceList(2)
                                .build())
                        .startDate("2020-06-14-00.00.00")
                        .endDate("2020-12-31-23.59.59")
                        .priority(0)
                        .price(new BigDecimal("33.33"))
                        .currency(Currency.EUR)
                        .build());
    }
}
