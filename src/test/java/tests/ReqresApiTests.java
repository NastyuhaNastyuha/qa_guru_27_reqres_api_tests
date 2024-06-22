package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.ReqresSpec.*;

@DisplayName("API-тесты для reqres.in")
public class ReqresApiTests extends TestBase {

    @DisplayName("Успешное создание пользователя")
    @Tag("reqres")
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

    @DisplayName("Успешное получение одного пользователя")
    @Tag("reqres")
    @Test
    void successfulGetSingleUserTest() {
        int userId = 3;
        String userEmail = "emma.wong@reqres.in";
        String userFirstName = "Emma";
        String userLastName = "Wong";

        GetUserResponseModel response = step("Отправить запрос на получение данных одного пользователя", () ->
                given(requestSpec)

                        .when()
                        .get("/users/" + userId)

                        .then()
                        .spec(responseStatusCode200Spec)
                        .extract().as(GetUserResponseModel.class));


        step("Проверить ответ", () -> {
            assertThat(response.getData().getEmail()).isEqualTo(userEmail);
            assertThat(response.getData().getFirstName()).isEqualTo(userFirstName);
            assertThat(response.getData().getLastName()).isEqualTo(userLastName);
        });
    }

    @DisplayName("Успешное редактирование пользователя")
    @Tag("reqres")
    @Test
    void successfulUpdateUserTest() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = simpleDateFormat.format(date.getTime());
        int userId = 2;

        String userJob = "engineer";

        UpdateUserBodyModel userData = new UpdateUserBodyModel();
        userData.setJob(userJob);

        UpdateUserResponseModel response = step("Отправить запрос на редактирование должности пользователя",
                () -> given(requestSpec)
                        .body(userData)

                        .when()
                        .patch("/users/" + userId)

                        .then()
                        .spec(responseStatusCode200Spec)
                        .extract().as(UpdateUserResponseModel.class));
        step("Проверить ответ", () -> {
            assertThat(response.getJob()).isEqualTo(userJob);
            assertThat(response.getUpdatedAt()).contains(todayDate);
        });
    }


    @DisplayName("Невозможно зарегистрировать пользователя без пароля")
    @Tag("reqres")
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

    @DisplayName("Успешное удаление пользователя")
    @Tag("reqres")
    @Test
    void successfulDeleteUserTest() {
        int userId = 2;

        step("Отправить запрос на удаление пользователя и проверить ответ", () ->
                given(requestSpec)

                        .when()
                        .delete("/users/" + userId)

                        .then()
                        .spec(responseStatusCode204Spec));
    }
}