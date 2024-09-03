package tests;

import io.qameta.allure.*;
import models.BadRequestResponseModel;
import models.LoginResponseModel;
import models.RegisterUserBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.ReqresSpec.*;

@Epic("Регистрация и авторизация")
@Story("Авторизация пользователя")
@Feature("Авторизация пользователя")
@DisplayName("Тесты на авторизацию пользователя")
public class LoginTests extends TestBase {
    @DisplayName("Успешная авторизация пользователя")
    @Tag("reqres")
    @Owner("rybinaa")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    void successfulLoginTest() {
        String email = "eve.holt@reqres.in";
        String password = "cityslicka";

        RegisterUserBodyModel userData = new RegisterUserBodyModel();
        userData.setEmail(email);
        userData.setPassword(password);

        LoginResponseModel response = step("Отправить запрос на авторизацию пользователя", () ->
                given(requestSpec).body(userData)

                .when().post("/login")

                .then().spec(responseStatusCode200Spec).extract().as(LoginResponseModel.class));

        step("Проверить ответ", () -> {
            assertThat(response.getToken()).isAlphanumeric();
            assertThat(response.getToken()).hasSizeGreaterThanOrEqualTo(17);
        });
    }

    @DisplayName("Невозможно авторизоваться без пароля")
    @Tag("reqres")
    @Owner("rybinaa")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    void unsuccessfulLoginTest() {
        String email = "peter@klaven";
        String errorMessage = "Missing password";

        RegisterUserBodyModel userData = new RegisterUserBodyModel();
        userData.setEmail(email);

        BadRequestResponseModel response = step("Отправить запрос на авторизацию пользователя без пароля", () ->
                given(requestSpec).body(userData)

                .when().post("/login")

                .then().spec(responseStatusCode400Spec).extract().as(BadRequestResponseModel.class));

        step("Проверить ответ", () -> {
            assertThat(response.getError()).isEqualTo(errorMessage);
        });
    }
}