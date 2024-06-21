package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.CreateUserSpec.createUserRequestSpec;
import static specs.CreateUserSpec.createUserResponseSpec;
import static specs.DeleteUserSpec.deleteUserRequestSpec;
import static specs.DeleteUserSpec.deleteUserResponseSpec;
import static specs.GetUserSpec.getUserRequestSpec;
import static specs.GetUserSpec.getUserResponseSpec;
import static specs.RegisterUserSpec.*;
import static specs.UpdateUserSpec.updateUserRequestSpec;
import static specs.UpdateUserSpec.updateUserResponseSpec;

@DisplayName("API-тесты для reqres.in")
public class ReqresApiTests extends TestBase {

    @DisplayName("Успешное создание пользователя")
    @Test
    void successfulCreateUser() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = simpleDateFormat.format(date.getTime());

        String userName = "Anastasiya";
        String userJob = "QA";

        CreateUserBodyModel userData = new CreateUserBodyModel();
        userData.setName(userName);
        userData.setUserJob(userJob);

        CreateUserResponseModel response = step("Make request", () ->
                given(createUserRequestSpec)
                        .body(userData)

                        .when()
                        .post("/users")

                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateUserResponseModel.class));

        step("Check response", () -> {
            assertThat(response.getName()).isEqualTo(userName);
            assertThat(response.getUserJob()).isEqualTo(userJob);
            assertThat(response.getId()).isNotNull();
            assertThat(response.getCreatedAt()).contains(todayDate);
        });
    }

    @DisplayName("Успешное получение одного пользователя")
    @Test
    void successfulGetSingleUser() {
        int userId = 3;
        String userEmail = "emma.wong@reqres.in";
        String userFirstName = "Emma";
        String userLastName = "Wong";

        GetUserResponseModel response = step("Make request", () ->
                given(getUserRequestSpec)

                        .when()
                        .get("/users/" + userId)

                        .then()
                        .spec(getUserResponseSpec)
                        .extract().as(GetUserResponseModel.class));


        step("Check response", () -> {
            assertThat(response.getData().getEmail()).isEqualTo(userEmail);
            assertThat(response.getData().getFirstName()).isEqualTo(userFirstName);
            assertThat(response.getData().getLastName()).isEqualTo(userLastName);
        });
    }

    @DisplayName("Успешное редактирование пользователя")
    @Test
    void successfulUpdateUser() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = simpleDateFormat.format(date.getTime());
        int userId = 2;

        String userJob = "engineer";

        UpdateUserBodyModel userData = new UpdateUserBodyModel();
        userData.setJob(userJob);

        UpdateUserResponseModel response = step("Make request", () ->
                given(updateUserRequestSpec)
                        .body(userData)

                        .when()
                        .patch("/users/" + userId)

                        .then()
                        .spec(updateUserResponseSpec)
                        .extract().as(UpdateUserResponseModel.class));
        step("Check response", () -> {
            assertThat(response.getJob()).isEqualTo(userJob);
            assertThat(response.getUpdatedAt()).contains(todayDate);
        });
    }


    @DisplayName("Невозможно зарегистрировать пользователя без пароля")
    @Test
    void unsuccessfulRegistrationUser() {
        String userEmail = "sydney@fife";
        String errorMessage = "Missing password";

        RegisterUserBodyModel userData = new RegisterUserBodyModel();
        userData.setEmail(userEmail);

        BadRequestResponseModel response = step("Make request", () ->
                given(registerUserRequestSpec)
                        .body(userData)

                        .when()
                        .post("/register")

                        .then()
                        .spec(registerUserErrorResponseSpec)
                        .extract().as(BadRequestResponseModel.class));

        step("Check response", () ->
                assertThat(response.getError()).isEqualTo(errorMessage));
    }

    @DisplayName("Успешное удаление пользователя")
    @Test
    void successfulDeleteUser() {
        int userId = 2;

        step("Make request and check status code", () ->
                given(deleteUserRequestSpec)

                        .when()
                        .delete("/users/" + userId)

                        .then()
                        .spec(deleteUserResponseSpec));
    }
}