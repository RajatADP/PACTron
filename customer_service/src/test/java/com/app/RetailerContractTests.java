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
import com.app.clients.CustomerServiceClient;
import com.app.models.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "RetailerService")
@PactDirectory("src/test/resources/contracts")
public class RetailerContractTests {
    @Autowired
    private CustomerServiceClient customerServiceClient;

    @BeforeAll
    public static void config() {
        System.setProperty("pact.writer.overwrite", "true");
    }

    @Pact(consumer = "CustomerCatalogue")
    public RequestResponsePact singleCustomer(PactDslWithProvider builder) {
        return builder
                .given("customer with ID 1 exists", "id", 1)
                .uponReceiving("get customer with ID 1")
                .path("/customer/1")
                .willRespondWith()
                .status(200)
                .body(
                        new PactDslJsonBody()
                                .integerType("id", 1L)
                                .stringType("name", "Marketo")
                )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "singleCustomer", pactVersion = PactSpecVersion.V3)
    void testSingleProduct(MockServer mockServer) {
        customerServiceClient.setBaseUrl(mockServer.getUrl());
        Customer customer = customerServiceClient.getCustomerById(1L);
        assertThat(customer, is(equalTo(new Customer(1L, "Marketo", null))));
    }
}
