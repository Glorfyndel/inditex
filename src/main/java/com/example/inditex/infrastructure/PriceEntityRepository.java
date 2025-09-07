package com.example.inditex.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PriceEntityRepository extends JpaRepository<PriceEntity, PriceEntity.Id> {

    @Query(nativeQuery = true, value = """
            SELECT *
            FROM prices p
            WHERE p.brand_id = :brandId
              AND p.product_id = :productId
              AND :applicationDate BETWEEN p.start_date AND p.end_date
            ORDER BY p.priority DESC
            LIMIT 1
            """)
    Optional<PriceEntity> findCurrentPrice(long brandId, long productId, String applicationDate);
}
