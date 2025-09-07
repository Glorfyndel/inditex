package com.example.inditex.application;

import com.example.inditex.domain.PriceNotFoundException;
import com.example.inditex.domain.ProductPriceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PricesController.class)
@Import(PricesControllerTest.Config.class)
class PricesControllerTest {

    @TestConfiguration
    static class Config {

    }

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductPriceService productPriceService;

    @DisplayName("Constraints validation brandId and productId must be positive")
    @Test
    void constraintsValidation() throws Exception {
        mockMvc.perform(get("/inditex/prices")
                        .param("brandId", "-1")
                        .param("productId", "-35455")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.constraints['getPrices.brandId']")
                        .value("must be greater than or equal to 0"))
                .andExpect(jsonPath("$.constraints['getPrices.productId']")
                        .value("must be greater than or equal to 0"));
    }

    @DisplayName("Incorrect date time format")
    @Test
    void incorrectDateTimeFormat() throws Exception {
        mockMvc.perform(get("/inditex/prices")
                .param("brandId", "1")
                .param("productId", "35455")
                .param("applicationDate", "2020-06-14 10:00:00"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.detail")
                        .value("Failed to convert 'applicationDate' with value: '2020-06-14 10:00:00'"));
    }

    @DisplayName("Price not found")
    @Test
    void notFound() throws Exception {
        when(productPriceService.getProductPrice(eq(1L), eq(35455L), eq(LocalDateTime.parse("2020-06-14T10:00:00"))))
                .thenThrow(new PriceNotFoundException("%s - %s - %s".formatted(1L, 35455L, "2020-06-14T10:00:00")));

        mockMvc.perform(get("/inditex/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.detail")
                        .value("No price found for criteria: 1 - 35455 - 2020-06-14T10:00:00"));
    }
}
