package com.example.inditex.infrastructure;

import com.example.inditex.domain.PriceNotFoundException;
import com.example.inditex.domain.Price;
import com.example.inditex.domain.PricesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PricesServiceImpl implements PricesService {

    private final PriceEntityRepository priceEntityRepository;

    @Override
    @Transactional(readOnly = true)
    public Price getProductPrice(long brandId, long productId, LocalDateTime applicationDate) {
        final PriceEntity currentPrice = priceEntityRepository.findCurrentPrice(brandId, productId, applicationDate.format(formatter))
                .orElseThrow(() -> new PriceNotFoundException("%s - %s - %s".formatted(brandId, productId, applicationDate)));

        return Price.builder()
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
