package com.example.inditex.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface ProductPriceService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss");

    ProductPrice getProductPrice(long brandId, long productId, LocalDateTime applicationDate);
}
