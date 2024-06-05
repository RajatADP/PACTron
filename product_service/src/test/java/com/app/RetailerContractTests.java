package com.app;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactDirectory;
import com.app.clients.ProductServiceClient;
import com.app.models.Product;
import com.app.models.ProductServiceResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "RetailerService")
@PactDirectory("src/test/resources/contracts")
public class RetailerContractTests {
    @Autowired
    private ProductServiceClient productServiceClient;

    @BeforeAll
    public static void config() {
        System.setProperty("pact.writer.overwrite", "true");
    }


    @Pact(consumer = "ProductCatalogue")
    public RequestResponsePact allProducts(PactDslWithProvider builder) {
        return builder
                .given("products exists")
                .uponReceiving("get all products")
                .path("/products")
                .willRespondWith()
                .status(200)
                .body(
                        Objects.requireNonNull(Objects.requireNonNull(new PactDslJsonBody()
                                        .minArrayLike("products", 1, 2)
                                        .integerType("id", 9L)
                                        .stringType("name", "Gem Visa")
                                        .stringType("type", "CREDIT_CARD")
                                        .closeObject())
                                .closeArray())
                )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "allProducts", pactVersion = PactSpecVersion.V3)
    void testAllProducts(MockServer mockServer) {
        productServiceClient.setBaseUrl(mockServer.getUrl());
        List<Product> products = productServiceClient.fetchProducts().getProducts();
        assertThat(products, hasSize(2));
        assertThat(products.stream().findFirst().get().getId(), is(equalTo(9L)));
    }

    @Pact(consumer = "ProductCatalogue")
    public RequestResponsePact singleProduct(PactDslWithProvider builder) {
        return builder
                .given("product with ID 10 exists", "id", 10)
                .uponReceiving("get product with ID 10")
                .path("/product/10")
                .willRespondWith()
                .status(200)
                .body(
                        new PactDslJsonBody()
                                .integerType("id", 10L)
                                .stringType("name", "28 Degrees")
                                .stringType("type", "CREDIT_CARD")
                                .stringType("code", "Code_001")
                                .stringType("version", "v1")
                )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "singleProduct", pactVersion = PactSpecVersion.V3)
    void testSingleProduct(MockServer mockServer) {
        productServiceClient.setBaseUrl(mockServer.getUrl());
        Product product = productServiceClient.getProductById(10L);
        assertThat(product.getId(), is(equalTo(10L)));
    }

    @Pact(consumer = "ProductCatalogue")
    public RequestResponsePact noProducts(PactDslWithProvider builder) {
        return builder
                .given("no products exists")
                .uponReceiving("get all products")
                .path("/products")
                .willRespondWith()
                .status(200)
                .body(
                        new PactDslJsonBody().array("products")
                )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "noProducts", pactVersion = PactSpecVersion.V3)
    void testNoProducts(MockServer mockServer) {
        productServiceClient.setBaseUrl(mockServer.getUrl());
        ProductServiceResponse products = productServiceClient.fetchProducts();
        assertThat(products.getProducts(), hasSize(0));
    }

    @Pact(consumer = "ProductCatalogue")
    public RequestResponsePact singleProductNotExists(PactDslWithProvider builder) {
        return builder
                .given("product with ID 10 does not exist", "id", 10)
                .uponReceiving("get product with ID 10")
                .path("/product/10")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "singleProductNotExists", pactVersion = PactSpecVersion.V3)
    void testSingleProductNotExists(MockServer mockServer) {
        productServiceClient.setBaseUrl(mockServer.getUrl());
        try {
            productServiceClient.getProductById(10L);
            fail("Expected service call to throw an exception");
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getMessage(), containsString("404 Not Found"));
        }
    }
}
