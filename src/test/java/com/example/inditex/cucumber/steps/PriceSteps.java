package com.example.inditex.cucumber.steps;

import com.example.inditex.application.PriceResponse;
import com.example.inditex.cucumber.CucumberSpringConfiguration;
import com.example.inditex.domain.Currency;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
public class PriceSteps extends CucumberSpringConfiguration {
    private PriceResponse priceResponse;
    protected RestClient restClient;

    @Before
    public void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }


    @Given("the application is running")
    public void the_application_is_running() {
    }

    @When("I request the price for brand {int}, product {int} at {string}")
    public void i_request_the_price(int brandId, int productId, String applicationDate) {
        priceResponse = restClient.get()
                .uri("/inditex/prices?brandId={brandId}&productId={productId}&applicationDate={applicationDate}",
                        brandId, productId, LocalDateTime.parse(applicationDate))
                .retrieve()
                .toEntity(PriceResponse.class)
                .getBody();
    }

    @Then("the response should be:")
    public void the_response_should_be(DataTable dataTable) {
        Map<String, String> expected = dataTable.asMaps().get(0);
        assertThat(priceResponse)
                .isEqualTo(PriceResponse.builder()
                        .brandId(Long.parseLong(expected.get("brandId")))
                        .productId(Long.parseLong(expected.get("productId")))
                        .priceList(Integer.parseInt(expected.get("priceList")))
                        .startDate(LocalDateTime.parse(expected.get("startDate")))
                        .endDate(LocalDateTime.parse(expected.get("endDate")))
                        .price(new BigDecimal(expected.get("price")))
                        .currency(Currency.valueOf(expected.get("currency")))
                        .build());
    }
}