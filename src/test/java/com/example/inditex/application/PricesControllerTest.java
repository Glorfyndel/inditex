package com.example.inditex.application;

import com.example.inditex.domain.ProductPriceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


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

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ProductPriceService productPriceService;

    @Test
    void brandIdNegative() throws Exception {
        mockMvc.perform(get("/inditex/prices")
                        .param("brandId", "-1")
                        .param("productId", "-35455")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.constraints['brandId']")
                        .value("must be greater than or equal to 0"));
    }

}