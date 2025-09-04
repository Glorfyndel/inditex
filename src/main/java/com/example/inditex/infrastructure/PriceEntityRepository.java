package com.example.inditex.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PriceEntityRepository extends JpaRepository<PriceEntity, PriceEntity.Id> {

    @Query("""
            SELECT p
            FROM PriceEntity p
            WHERE p.id.brandId = :brandId
              AND p.id.productId = :productId
              AND :applicationDate BETWEEN p.startDate AND p.endDate
            ORDER BY p.priority DESC
            LIMIT 1
            """)
    PriceEntity findCurrentPrice(long brandId, long productId, LocalDateTime applicationDate);
}
