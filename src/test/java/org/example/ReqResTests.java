package org.example;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.SoftAssertions;
import org.example.config.AtConfig;
import org.example.model.User;
import org.example.service.ReqResService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AtConfig.class)
public class ReqResTests {

  @Autowired
  private ReqResService reqResService;

  @Feature("Используя сервис с тестовым REST-API https://reqres.in/, написать 2 автотеста")
  @Story("1. GET https://reqres.in/api/users?page=2")
  @DisplayName("1. GET https://reqres.in/api/users?page=2")
  @Test
  void getUsersPage() {
    SoftAssertions softly = new SoftAssertions();

    List<User> users = reqResService.requstGetListUsers(2)
        .checkStatusCode(200)
        .getUsersByResponse();

    for (User user : users) {
      softly.assertThat(user.getEmail()).isNotNull();
      softly.assertThat(user.getLastName()).isNotNull();
    }
    softly.assertAll();
  }

  static Stream<User> pathToMethod() {
    List<User> users = new ArrayList<>();
    users.add(new User(1, "FirstName1", "LastName1", "user1@user.com", "https://reqres.in/img1.jpg"));
    users.add(new User(2, "FirstName2", "LastName2", "user2@user.com", "https://reqres.in/img2.jpg"));
    return users.stream();
  }

  @Feature("Используя сервис с тестовым REST-API https://reqres.in/, написать 2 автотеста")
  @Story("2. POST https://reqres.in/api/users")
  @ParameterizedTest(name = "Создание пользователя - {index}")
  @MethodSource("pathToMethod")
  void createUser(User user) {

    User resultUser = reqResService.requestCreateUser(user)
        .checkStatusCode(201)
        .getUserByResponse();

    assertThat(resultUser)
        .withFailMessage("Данные пользователя в ответе не совпадают с изначально направленными")
        .isEqualTo(user);
  }
}
