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
import com.app.model.Customer;
import com.app.service.RetailerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("RetailerService")
@Consumer("CustomerCatalogue")
//@PactFolder("src/main/resources/pacts")
@PactBroker(url = "http://localhost:9292")
public class CustomerServiceContractTests {
    @LocalServerPort
    public int port;

    @BeforeEach
    void setup(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State(value = "customer with ID 1 exists", action = StateChangeAction.SETUP)
    void customerExists(Map<String, Object> params) {
        RetailerService mock = Mockito.mock(RetailerService.class);
        Customer customer = new Customer(1L, "Marketo");
        long customerId = ((Number) params.get("id")).longValue();
        Mockito.when(mock.getCustomerDetails(customerId))
                .thenReturn(customer);
    }
}
