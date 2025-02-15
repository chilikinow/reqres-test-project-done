package org.example.service;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Аннотация @Service в Spring Framework используется для указания того, что класс является сервисным компонентом.
 * Это специальный тип класса, который содержит бизнес-логику приложения или предоставляет сервисные методы для других
 * компонентов. Классы, помеченные @Service, автоматически обнаруживаются Spring-контейнером в процессе сканирования
 * компонентов и регистрируются как бины.
 */
@Service
public class ReqResService {

  @Value("${api.endpoints.users}")
  private String usersEndpoint;

  private Response response;

  @Step("Выполнить GET запрос к /api/users?page={page}")
  public ReqResService requstGetListUsers(@NotNull Integer page) {
    response = RestAssured.given()
        .queryParam("page", Integer.valueOf(page))
        .when()
        .get(usersEndpoint);
    return this;
  }

  @Step("Проверить статус код, ожидаемое значение: {statusCode}")
  public ReqResService checkStatusCode(@NotNull Integer statusCode) {
    if (response != null) {
      response.then().statusCode(statusCode);
    }
    return this;
  }

  @Step("Получить список пользователей из ответа")
  public List<User> getUsersByResponse() {
    List<User> users = new ArrayList<>();
    if (response != null) {
      users = response.jsonPath().getList("data", User.class);
    }
    return users;
  }

  @Step("Выполнить POST запрос к /api/users для создания пользователя: {user}")
  public ReqResService requestCreateUser(@NotNull User user) {
    response = RestAssured.given()
        .header("content-type", "application/json; charset=utf-8")
        .body(user)
        .when()
        .post(usersEndpoint);
    return this;
  }

  @Step("Получить пользователя из ответа")
  public User getUserByResponse() {
    User user = new User();
    if (response != null) {
      user = response.as(User.class);
    }
    return user;
  }
}