package com.example.inditex.infrastructure;

import com.example.inditex.domain.Currency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prices")
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriceEntity implements Serializable {
    @EmbeddedId
    private Id id;

    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd-HH.mm.ss")
    private LocalDateTime endDate;

    private BigDecimal price;

    private Currency currency;

    private int priority;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Id implements Serializable {
        private long brandId;
        private long productId;
        private int priceList;
    }
}
