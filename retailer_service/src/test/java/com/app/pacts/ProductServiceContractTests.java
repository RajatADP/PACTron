package com.app.pacts;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.StateChangeAction;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.app.model.Product;
import com.app.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("RetailerService")
@Consumer("ProductCatalogue")
//@PactFolder("src/main/resources/pacts")
@PactBroker(url = "http://localhost:9292")
public class ProductServiceContractTests {
    @LocalServerPort
    public int port;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State(value = "products exists", action = StateChangeAction.SETUP)
    void productsExists() {
        productRepository.deleteAll();
        productRepository.saveAll(Arrays.asList(
                new Product(100L, "Test Product 1", "CREDIT_CARD", "v1", "CC_001"),
                new Product(200L, "Test Product 2", "CREDIT_CARD", "v1", "CC_002"),
                new Product(300L, "Test Product 3", "PERSONAL_LOAN", "v1", "PL_001"),
                new Product(400L, "Test Product 4", "SAVINGS", "v1", "SA_001")
        ));
    }

    @State(value = "no products exists", action = StateChangeAction.SETUP)
    void noProductsExist() {
        productRepository.deleteAll();
    }

    @State(value = "product with ID 10 exists", action = StateChangeAction.SETUP)
    void productExists(Map<String, Object> params) {
        long productId = ((Number) params.get("id")).longValue();
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            productRepository.save(new Product(productId, "Product", "TYPE", "v1", "001"));
        }
    }

    @State(value = "product with ID 10 does not exist", action = StateChangeAction.SETUP)
    void productNotExist(Map<String, Object> params) {
        long productId = ((Number) params.get("id")).longValue();
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            productRepository.deleteById(productId);
        }
    }
}
