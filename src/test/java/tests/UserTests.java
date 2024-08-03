package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import models.GetUserResponseModel;
import models.UpdateUserBodyModel;
import models.UpdateUserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.ReqresSpec.*;

@Epic("Взаимодействие с пользователем")
@Story("Просмотр, редактирование, удаление пользователя")
public class UserTests {
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

    @DisplayName("Успешное частичное редактирование пользователя")
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
