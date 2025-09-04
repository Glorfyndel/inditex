package com.example.inditex.application;

import com.example.inditex.domain.ProductPrice;
import com.example.inditex.domain.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/inditex")
@RequiredArgsConstructor
public class PricesController {
    private final ProductPriceService productPriceService;

    @GetMapping("/prices")
    public PriceResponse getPrices(@RequestParam long brandId, @RequestParam long productId, @RequestParam LocalDateTime applicationDate) {
        final ProductPrice productPrice = productPriceService.getProductPrice(brandId, productId, applicationDate);

        final PriceResponse priceResponse = PriceResponse.builder()
                .brandId(productPrice.brandId())
                .productId(productPrice.productId())
                .priceList(productPrice.priceList())
                .startDate(productPrice.startDate())
                .endDate(productPrice.endDate())
                .price(productPrice.price())
                .currency(productPrice.currency())
                .build();

        return priceResponse;
    }
}
