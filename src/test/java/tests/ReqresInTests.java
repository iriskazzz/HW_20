package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.ReqresSpec.requestSpec;
import static specs.ReqresSpec.responseSpec;

public class ReqresInTests extends TestBase {

  @Test
  @DisplayName("Получение списка пользователей")
  void checkListUsersTest() {
    ListUsersResponseModel response = step("Список пользователей", () ->
            given(requestSpec)
                    .when()
                    .get("/users?page=2")
                    .then()
                    .spec(responseSpec)
                    .statusCode(200)
                    .extract().as(ListUsersResponseModel.class)
    );

    step("Проверка данных из ответа", () -> {
      assertEquals(2, response.getTotalPages());
      assertEquals("https://reqres.in/#support-heading", response.getSupport().getUrl());
    });
  }

  @Test
  @DisplayName("Проверка создания пользователя")
  void checkCreateTest() {
    CreateBodyModel CreateBody = new CreateBodyModel();
    CreateBody.setName("morpheus");
    CreateBody.setJob("leader");

    CreateResponseModel response = step("Создание пользователя", () ->
            given(requestSpec)
                    .body(CreateBody)
                    .when()
                    .post("/users")
                    .then()
                    .spec(responseSpec)
                    .statusCode(201)
                    .extract().as(CreateResponseModel.class)
    );

    step("Проверка данных из ответа", () -> {
      assertEquals("morpheus", response.getName());
      assertEquals("leader", response.getJob());
    });
  }

  @Test
  @DisplayName("Проверка изменения данных пользователя")
  void checkUpdateTest() {
    CreateBodyModel CreateBody = new CreateBodyModel();
    CreateBody.setName("morpheus");
    CreateBody.setJob("zion resident");

    CreateResponseModel response = step("Изменение данных пользователя", () ->
            given(requestSpec)
                    .body(CreateBody)
                    .when()
                    .put("/users/2")
                    .then()
                    .spec(responseSpec)
                    .statusCode(200)
                    .extract().as(CreateResponseModel.class)
    );

    step("Проверка данных из ответа", () -> {
      assertEquals("morpheus", response.getName());
      assertEquals("zion resident", response.getJob());
    });
  }

  @Test
  @DisplayName("Проверка удаления пользователя")
  void checkDeleteTest() {
    given(requestSpec)
            .when()
            .delete("/users/65")
            .then()
            .spec(responseSpec)
            .statusCode(204);
  }

  @Test
  @DisplayName("Проверка неуспешной авторизации без пароля")
  void checkLoginUnsuccessfulTest() {
    LoginBodyModel LoginBody = new LoginBodyModel();
    LoginBody.setEmail("peter@klaven");

    ErrorModel response = step("Вызов метода авторищации", () ->
            given(requestSpec)
                    .body(LoginBody)
                    .when()
                    .post("/login")
                    .then()
                    .spec(responseSpec)
                    .statusCode(400)
                    .extract().as(ErrorModel.class)
    );

    step("Проверка данных из ответа", () -> {
      assertEquals("Missing password", response.getError());
    });
  }

}
