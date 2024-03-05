package org.example;

import io.github.microcks.testcontainers.MicrocksContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.io.File;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Testcontainers
public class CheckoutServiceTest {

    // service dealing with PaymentMethods (i.e. get paymentMethod by Id)
    @Autowired
    CheckoutService checkoutService;

    @Container
    public static MicrocksContainer microcksContainer = new MicrocksContainer("quay.io/microcks/microcks-uber:1.8.1");

    @BeforeAll
    public static void setup() throws Exception {
        microcksContainer.importAsMainArtifact(new File("src/test/resources/checkout-basic.yaml"));
        //microcksContainer.importAsSecondaryArtifact(new File("src/test/resources/checkout-postman.json"));
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        String url = microcksContainer.getRestMockEndpoint("CheckoutBasic", "0.1.0");
        registry.add("baseUrl", () -> url);
    }

    @Test
    public void getPaymentMethod() throws Exception {

        PaymentMethod paymentMethod = checkoutService.getPaymentMethod("googlepay");

        assertEquals("googlepay", paymentMethod.getName());
        assertEquals("wallet", paymentMethod.getType());
    }

}
