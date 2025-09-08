package com.example.inditex.infrastructure;

import com.example.inditex.domain.Currency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "prices")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "startDate", "endDate", "price", "currency", "priority"})
@ToString(of = {"id", "startDate", "endDate", "price", "currency", "priority"})
@Getter
@Setter
@Builder
public class PriceEntity implements Serializable {
    @EmbeddedId
    private Id id;

    private String startDate;

    private String endDate;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "curr")
    private Currency currency;

    private int priority;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"brandId", "productId", "priceList"})
    @ToString(of = {"brandId", "productId", "priceList"})
    @Getter
    @Setter
    @Builder
    public static class Id implements Serializable {
        private long brandId;
        private long productId;
        private int priceList;
    }
}
