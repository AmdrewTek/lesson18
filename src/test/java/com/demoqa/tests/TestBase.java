package com.demoqa.tests;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {
  @BeforeAll
  public static void setup() {
    WebDriverManager.chromedriver().setup();
    Configuration.pageLoadStrategy = "eager";
    Configuration.baseUrl = "https://demoqa.com";
    RestAssured.baseURI = "https://demoqa.com";
  }

  @AfterEach
  void shutDown() {
    closeWebDriver();
  }
}
