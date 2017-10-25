package com.pressassociation.qa;


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-html-report"},
        glue = {"com.pressassociation.qa/stepdefs"},
        features = {"src/test/java/com/pressassociation/qa/features"})
public class CucumberTests {
}