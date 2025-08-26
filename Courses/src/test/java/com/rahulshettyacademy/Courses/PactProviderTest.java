package com.rahulshettyacademy.Courses;

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
