# Microservices-Contract-Testing-with-Pact

```sql
INSERT INTO APIDevelopSpringBoot.storage2 (aisle, author, book_name, id, isbn) VALUES(43, 'Shetty', 'Microservices', 'hrtge43', 'hrtge');
INSERT INTO APIDevelopSpringBoot.storage2 (aisle, author, book_name, id, isbn) VALUES(21, 'Shetty', 'Selenium', 'khuys21', 'khuys');
INSERT INTO APIDevelopSpringBoot.storage2 (aisle, author, book_name, id, isbn) VALUES(36, 'Shetty', 'Appium', 'ttefs36', 'ttefs');
```


```sql
INSERT INTO Courses.storage2 (course_name, category, id, price) VALUES('Appium', 'mobile', '12', 120);
INSERT INTO Courses.storage2 (course_name, category, id, price) VALUES('Microservices testing', 'api', '2', 23);
INSERT INTO Courses.storage2 (course_name, category, id, price) VALUES('Selenium', 'web', '3', 66);
````

# BooksCatalogue-CoursesCatalogue.json

<details>
  <summary>Click to expand for more details!</summary>
  
  ```json
    {
  "consumer": {
    "name": "BooksCatalogue"
  },
  "interactions": [
    {
      "description": "Appium course Does not exist",
      "providerStates": [
        {
          "name": "Course Appium does not exist",
          "params": {
            "name": "Appium"
          }
        }
      ],
      "request": {
        "method": "GET",
        "path": "/getCourseByName/Appium"
      },
      "response": {
        "status": 404
      }
    },
    {
      "description": "Get the Appium course details",
      "providerStates": [
        {
          "name": "Course Appium exist"
        }
      ],
      "request": {
        "method": "GET",
        "path": "/getCourseByName/Appium"
      },
      "response": {
        "body": {
          "category": "mobile",
          "price": 44
        },
        "headers": {
          "Content-Type": "application/json; charset=UTF-8"
        },
        "matchingRules": {
          "body": {
            "$.category": {
              "combine": "AND",
              "matchers": [
                {
                  "match": "type"
                }
              ]
            },
            "$.price": {
              "combine": "AND",
              "matchers": [
                {
                  "match": "integer"
                }
              ]
            }
          },
          "header": {
            "Content-Type": {
              "combine": "AND",
              "matchers": [
                {
                  "match": "regex",
                  "regex": "application/json(;\\s?charset=[\\w\\-]+)?"
                }
              ]
            }
          }
        },
        "status": 200
      }
    },
    {
      "description": "getting all courses details",
      "providerStates": [
        {
          "name": "courses exist"
        }
      ],
      "request": {
        "method": "GET",
        "path": "/allCourseDetails"
      },
      "response": {
        "body": [
          {
            "price": 10
          },
          {
            "price": 10
          },
          {
            "price": 10
          }
        ],
        "headers": {
          "Content-Type": "application/json; charset=UTF-8"
        },
        "matchingRules": {
          "body": {
            "$": {
              "combine": "AND",
              "matchers": [
                {
                  "match": "type",
                  "min": 3
                }
              ]
            },
            "$[*].price": {
              "combine": "AND",
              "matchers": [
                {
                  "match": "integer"
                }
              ]
            }
          },
          "header": {
            "Content-Type": {
              "combine": "AND",
              "matchers": [
                {
                  "match": "regex",
                  "regex": "application/json(;\\s?charset=[\\w\\-]+)?"
                }
              ]
            }
          }
        },
        "status": 200
      }
    }
  ],
  "metadata": {
    "pact-jvm": {
      "version": "4.2.7"
    },
    "pactSpecification": {
      "version": "3.0.0"
    }
  },
  "provider": {
    "name": "CoursesCatalogue"
  }
}

  ```
</details>


# Provider Test cases:

```java
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rahulshettyacademy.controller.AllCourseData;
import com.rahulshettyacademy.repository.CoursesRepository;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.StateChangeAction;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("CoursesCatalogue")
@PactFolder("src/test/resources/pacts")
public class PactProviderTest {
    @LocalServerPort
    private int port;

    @Autowired
    private CoursesRepository repository;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    public void pactVerificationTest(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    public void setup(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @State(value = "courses exist", action = StateChangeAction.SETUP)
    public void coursesExist() {

    }

    @State(value = "courses exist", action = StateChangeAction.TEARDOWN)
    public void coursesExistTearDown() {

    }
    //one last step- setup method- Appium - insert a record in test
    //teardown - appium

    //   -  /getCourseName/Appium () -> {name -appium, id="", price ="}
    //      /getCourseName/Appium - > { msg : course do not exist}

    @State(value = "Course Appium exist", action = StateChangeAction.SETUP)
    public void appiumCourseExist() {
        //appium
    }

    @State(value = "Course Appium exist", action = StateChangeAction.TEARDOWN)
    public void appiumCourseExistTearDown() {
        //
    }

    @State(value = "Course Appium does not exist", action = StateChangeAction.SETUP)
    public void appiumCourseDoNotExist(Map<String, Object> params) {
        String name = (String) params.get("name");
        Optional<AllCourseData> allCourseData = repository.findById(name);//mock
        if (allCourseData.isPresent()) {
            repository.deleteById("Appium");
        }
    }

    @State(value = "Course Appium does not exist", action = StateChangeAction.TEARDOWN)
    public void appiumCourseDoNotExistTearDown(Map<String, Object> params) {
        // add appium record in database
        String name = (String) params.get("name");
        Optional<AllCourseData> deleteOptional = repository.findById(name);//mock
        if (deleteOptional.isEmpty()) {
            AllCourseData allCourseData = new AllCourseData();
            allCourseData.setCourse_name("Appium");
            allCourseData.setCategory("mobile");
            allCourseData.setPrice(120);
            allCourseData.setId("12");
            repository.save(allCourseData);
        }
    }
}
```

# Consumer Testing

```java
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
```


