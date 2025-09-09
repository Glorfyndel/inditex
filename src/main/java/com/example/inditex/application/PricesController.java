package com.example.inditex.application;

import com.example.inditex.domain.Price;
import com.example.inditex.domain.PricesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "Price", description = "API for Inditex product prices")
@RestController
@RequestMapping("/inditex")
@RequiredArgsConstructor
@Validated
public class PricesController {
    private final PricesService pricesService;

    @Operation(summary = "Get price information based on brand, product and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomRestExeptionHandler.ConstraintsErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Price not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/prices")
    public PriceResponse getPrices(@Parameter(required = true, example = "1") @RequestParam @NotNull @Min(0) long brandId,
                                   @Parameter(required = true, example = "35455") @RequestParam @NotNull @Min(0) long productId,
                                   @Parameter(required = true, example = "2020-06-14T10:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {
        final Price price = pricesService.getProductPrice(brandId, productId, applicationDate);

        return PriceResponse.builder()
                .brandId(price.brandId())
                .productId(price.productId())
                .priceList(price.priceList())
                .startDate(price.startDate())
                .endDate(price.endDate())
                .price(price.price())
                .currency(price.currency())
                .build();
    }
}
