package org.example.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static io.restassured.filter.log.LogDetail.ALL;

/**
 * Аннотация @Configuration в Spring Framework используется для указания того, что класс является конфигурационным,
 * который определяет бины (beans) и настройки для Spring-контейнера. Классы, помеченные этой аннотацией,
 * содержат методы, аннотированные @Bean, которые возвращают объекты, управляемые Spring-контейнером.
 */
@Configuration
@ComponentScan("org.example")
public class RestAssuredConfig {

  @Value("${api.base-url}")
  private String baseUrl;

  /**
   * Аннотация @Bean в Spring Framework используется для указания того, что метод создает и возвращает объект,
   * который должен быть управляемым бином (bean) в Spring-контейнере. Бин — это объект, который Spring создает,
   * управляет его жизненным циклом и внедряет в другие компоненты приложения.
   */
  @Bean
  public AllureRestAssured allureRestAssured() {
    final AllureRestAssured FILTER = new AllureRestAssured();
    FILTER.setRequestTemplate("request.ftl");
    FILTER.setResponseTemplate("response.ftl");
    return FILTER;
  }

  @Bean
  public RequestSpecification requestSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(baseUrl)
        .addHeader("Content-Type", "application/json")
        .addFilter(allureRestAssured())
        .build();
  }

  /**
   * Аннотация @PostConstruct используется в Java (в том числе в Spring Framework) для указания метода,
   * который должен быть выполнен после создания бина и внедрения всех его зависимостей.
   * Этот метод вызывается только один раз в жизненном цикле бина и используется для выполнения инициализации,
   * например, настройки объекта, загрузки данных или проверки зависимостей.
   */
  @PostConstruct
  private void postConstruct() {
    RestAssured.requestSpecification = requestSpec();
    RestAssured.useRelaxedHTTPSValidation();
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(ALL);
  }
}