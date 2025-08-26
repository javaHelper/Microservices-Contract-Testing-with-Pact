package com.rahulshettyacademy;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahulshettyacademy.controller.LibraryController;
import com.rahulshettyacademy.controller.ProductsPrices;
import com.rahulshettyacademy.controller.SpecificProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
public class PactConsumerTest {

    @Autowired
    private LibraryController libraryController;

    @Autowired
    private ObjectMapper mapper;

    @Pact(consumer = "BooksCatalogue")
    public V4Pact PactallCoursesDetailsPriceCheck(PactBuilder builder) {
        return builder
                .usingLegacyDsl()
                .given("courses exist")
                .uponReceiving("getting all courses details")
                .path("/allCourseDetails")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(PactDslJsonArray.arrayMinLike(3)
                        .integerType("price", 10)
                        .closeObject())
                .toPact(V4Pact.class);
    }

    @Pact(consumer = "BooksCatalogue")
    public V4Pact getCourseByName(PactBuilder builder) {
        return builder
                .usingLegacyDsl()
                .given("Course Appium exist")
                .uponReceiving("Get the Appium course details")
                .path("/getCourseByName/Appium")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .integerType("price", 44)
                        .stringType("category", "mobile"))
                .toPact(V4Pact.class);
    }

    @Pact(consumer = "BooksCatalogue")
    public V4Pact getCourseByNameNotExist(PactBuilder builder) {
        return builder
                .usingLegacyDsl()
                .given("Course Appium does not exist", "name", "Appium")
                .uponReceiving("Appium course Does not exist")
                .path("/getCourseByName/Appium")
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(providerName = "CoursesCatalogue", pactMethod = "PactallCoursesDetailsPriceCheck", port = "9999")
    public void testAllProductsSum(MockServer mockServer) throws Exception {
        libraryController.setBaseUrl(mockServer.getUrl());

        ProductsPrices productsPrices = libraryController.getProductPrices();

        String expectedJson = """
                {
                  "booksPrice": 250,
                  "coursesPrice": 30
                }
                """;

        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(mapper.writeValueAsString(productsPrices));

        Assertions.assertEquals(expectedNode, actualNode);
    }

    @Test
    @PactTestFor(providerName = "CoursesCatalogue", pactMethod = "getCourseByName", port = "9999")
    void testByProductName(MockServer mockServer) throws Exception {
        libraryController.setBaseUrl(mockServer.getUrl());

        SpecificProduct specificProduct = libraryController.getProductFullDetails("Appium");

        JsonNode expectedNode = mapper.readTree("""
                {
                  "product": {
                    "bookName": "Appium",
                    "id": "ttefs36",
                    "isbn": "ttefs",
                    "aisle": 36,
                    "author": "Shetty"
                  },
                  "price": 44,
                  "category": "mobile"
                }
                """);

        JsonNode actualNode = mapper.readTree(mapper.writeValueAsString(specificProduct));
        Assertions.assertEquals(expectedNode, actualNode);
    }

    @Test
    @PactTestFor(providerName = "CoursesCatalogue", pactMethod = "getCourseByNameNotExist", port = "9999")
    public void testByProductNameNotExist(MockServer mockServer) throws Exception {
        libraryController.setBaseUrl(mockServer.getUrl());

        SpecificProduct specificProduct = libraryController.getProductFullDetails("Appium");

        String expectedJson = """
                {
                  "product": {
                    "bookName": "Appium",
                    "id": "ttefs36",
                    "isbn": "ttefs",
                    "aisle": 36,
                    "author": "Shetty"
                  },
                  "msg": "AppiumCategory and price details are not available at this time"
                }
                """;

        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(mapper.writeValueAsString(specificProduct));

        Assertions.assertEquals(expectedNode, actualNode);
    }
}
