package com.example.inditex.infrastructure;

import com.example.inditex.domain.PriceNotFoundException;
import com.example.inditex.domain.ProductPrice;
import com.example.inditex.domain.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService {

    private final PriceEntityRepository priceEntityRepository;

    @Override
    public ProductPrice getProductPrice(long brandId, long productId, LocalDateTime applicationDate) {
        final PriceEntity currentPrice = priceEntityRepository.findCurrentPrice(brandId, productId, applicationDate.format(formatter))
                .orElseThrow(() -> new PriceNotFoundException("%s - %s - %s".formatted(brandId, productId, applicationDate)));

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
