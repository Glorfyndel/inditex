package com.example.inditex.domain;

import com.example.inditex.infrastructure.PriceEntity;
import com.example.inditex.infrastructure.PriceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ProductPriceService {

    private final PriceEntityRepository priceEntityRepository;

    public ProductPrice getProductPrice(long brandId, long productId, LocalDateTime applicationDate) {
        final PriceEntity currentPrice = priceEntityRepository.findCurrentPrice(brandId, productId, applicationDate);

        return ProductPrice.builder()
                .brandId(currentPrice.getId().getBrandId())
                .productId(currentPrice.getId().getProductId())
                .priceList(currentPrice.getId().getPriceList())
                .startDate(currentPrice.getStartDate())
                .endDate(currentPrice.getEndDate())
                .price(currentPrice.getPrice())
                .priority(currentPrice.getPriority())
                .currency(currentPrice.getCurrency())
                .build();
    }
}
