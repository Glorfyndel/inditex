package com.example.inditex.domain;

import com.example.inditex.infrastructure.PriceEntity;
import com.example.inditex.infrastructure.PriceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class ProductPriceService {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss");
    private final PriceEntityRepository priceEntityRepository;

    public ProductPrice getProductPrice(long brandId, long productId, LocalDateTime applicationDate) {
        final PriceEntity currentPrice = priceEntityRepository.findCurrentPrice(brandId, productId, applicationDate.format(formatter));

        return ProductPrice.builder()
                .brandId(currentPrice.getId().getBrandId())
                .productId(currentPrice.getId().getProductId())
                .priceList(currentPrice.getId().getPriceList())
                .startDate(LocalDateTime.parse(currentPrice.getStartDate(), formatter))
                .endDate(LocalDateTime.parse(currentPrice.getEndDate(), formatter))
                .price(currentPrice.getPrice())
                .priority(currentPrice.getPriority())
                .currency(currentPrice.getCurrency())
                .build();
    }
}
