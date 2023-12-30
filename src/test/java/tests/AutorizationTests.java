package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.ReqresSpec.requestSpecification;
import static specs.ReqresSpec.responseSpecification;

@Owner("izolina")
@Feature("Авторизация пользователя")
public class AutorizationTests extends TestBase {

    @Test
    @DisplayName("Проверка успешной авторизации")
    void checkLoginSuccessfulTest() {
        LoginBodyModel loginBody = new LoginBodyModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("pistol");

        LoginResponseModel response = step("Вызов метода авторизации", () ->
                given(requestSpecification)
                        .body(loginBody)
                        .when()
                        .post("/login")
                        .then()
                        .spec(responseSpecification)
                        .statusCode(200)
                        .extract().as(LoginResponseModel.class)
        );

        step("Проверка данных из ответа", () ->
                assertEquals("QpwL5tke4Pnpja7X4", response.getToken()));
    }

    @Test
    @DisplayName("Проверка неуспешной авторизации без пароля")
    void checkLoginUnsuccessfulTest() {
        LoginBodyModel loginBody = new LoginBodyModel();
        loginBody.setEmail("peter@klaven");

        ErrorModel response = step("Вызов метода авторизации", () ->
                given(requestSpecification)
                        .body(loginBody)
                        .when()
                        .post("/login")
                        .then()
                        .spec(responseSpecification)
                        .statusCode(400)
                        .extract().as(ErrorModel.class)
        );

        step("Проверка данных из ответа", () ->
                assertEquals("Missing password", response.getError()));
    }

}
