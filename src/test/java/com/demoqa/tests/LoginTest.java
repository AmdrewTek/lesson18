package com.demoqa.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.demoqa.tests.TestData.login;
import static com.demoqa.tests.TestData.password;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class LoginTest extends TestBase {
  private static final String API_KEY = "reqres-free-v1";

  @Test
  public void successfulLoginTest() {
    open("/login");
    $("#userName").setValue(login);
    $("#password").setValue(password).pressEnter();
    $("#userName-value").shouldHave(text(login));

  }

  @Test
  public void successfulLoginWithApiTest() {
    String authData = "{\"userName\": \"qwe123\", \"password\": \"Qwe123qwe!\"}";

    Response authResponse = given()
      .header("x-api-key", API_KEY)
      .contentType(JSON)
      .body(authData)
      .log().all()
      .when()
      .post("Account/v1/Login")
      .then()
      .log().body()
      .statusCode(200)
      .extract().response();

    open("/favicon.ico");
    getWebDriver().manage().addCookie(new Cookie("userId", authResponse.jsonPath().getString("userId")));
    getWebDriver().manage().addCookie(new Cookie("expires", authResponse.jsonPath().getString("expires")));
    getWebDriver().manage().addCookie(new Cookie("token", authResponse.jsonPath().getString("token")));


    open("/profile");
    $("#userName-value").shouldHave(text(login));
  }
  @Test
  public void successfulLoginWithApiTest2() {
    String login = "qwe123";
    String password = "Qwe123qwe!";
    String authData = "{\"userName\": \"" + login + "\", \"password\": \"" + password + "\"}";

    Response authResponse = given()
      .header("x-api-key", API_KEY)
      .contentType(JSON)
      .body(authData)
      .log().all()
      .when()
      .post("/Account/v1/Login")
      .then()
      .log().body()
      .statusCode(200)
      .extract().response();

    String token = authResponse.jsonPath().getString("token");
    String userId = authResponse.jsonPath().getString("userId");

    // Открываем любой адрес на домене demoqa.com, чтобы иметь доступ к localStorage
    open("/favicon.ico");

    // Устанавливаем данные аутентификации в localStorage
    executeJavaScript("window.localStorage.setItem('token', arguments[0]);", token);
    executeJavaScript("window.localStorage.setItem('userID', arguments[0]);", userId);
    executeJavaScript("window.localStorage.setItem('userName', arguments[0]);", login);

    // Переходим на профиль — данные уже установлены
    open("/profile");

    // Проверка: имя пользователя на странице
    $("#userName-value").shouldHave(text(login));
  }
}

