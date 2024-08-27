package tests;

import io.qameta.allure.*;
import models.BadRequestResponseModel;
import models.CreateUserBodyModel;
import models.CreateUserResponseModel;
import models.RegisterUserBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.ReqresSpec.*;

@Epic("Регистрация и авторизация")
@Story("Регистрация пользователя")
@Feature("Регистрация пользователя")
public class RegistrationTests extends  TestBase{
    @DisplayName("Успешное создание пользователя")
    @Tag("reqres")
    @Owner("rybinaa")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    void successfulCreateUserTest() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = simpleDateFormat.format(date.getTime());

        String userName = "Anastasiya";
        String userJob = "QA";

        CreateUserBodyModel userData = new CreateUserBodyModel();
        userData.setName(userName);
        userData.setUserJob(userJob);

        CreateUserResponseModel response = step("Отправить запрос на создание пользователя", () ->
                given(requestSpec)
                        .body(userData)

                        .when()
                        .post("/users")

                        .then()
                        .spec(responseStatusCode201Spec)
                        .extract().as(CreateUserResponseModel.class));

        step("Проверить ответ", () -> {
            assertThat(response.getName()).isEqualTo(userName);
            assertThat(response.getUserJob()).isEqualTo(userJob);
            assertThat(response.getId()).isNotNull();
            assertThat(response.getCreatedAt()).contains(todayDate);
        });
    }

    @DisplayName("Невозможно зарегистрировать пользователя без пароля")
    @Tag("reqres")
    @Owner("rybinaa")
    @Severity(SeverityLevel.NORMAL)
    @Test
    void unsuccessfulRegistrationUserTest() {
        String userEmail = "sydney@fife";
        String errorMessage = "Missing password";

        RegisterUserBodyModel userData = new RegisterUserBodyModel();
        userData.setEmail(userEmail);

        BadRequestResponseModel response = step("Отправить запрос регистрации пользователя без пароля", () ->
                given(requestSpec)
                        .body(userData)

                        .when()
                        .post("/register")

                        .then()
                        .spec(responseStatusCode400Spec)
                        .extract().as(BadRequestResponseModel.class));

        step("Проверить ответ", () ->
                assertThat(response.getError()).isEqualTo(errorMessage));
    }
}
