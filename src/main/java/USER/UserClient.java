package USER;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient extends BurgerRestClient {
    private static final String USER_PATH = "api/auth/";

    @Step("Register user")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }

    @Step("Login courier")
    public ValidatableResponse login(UserCredentials creds) {
        return given()
                .spec(getBaseSpec())
                .body(creds)
                .when()
                .post(USER_PATH + "login")
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse delete( String accessToken) {
        return given().log().all()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .delete(USER_PATH + "user")
                .then();
    }

    @Step("Update user")
    public ValidatableResponse update(UpdateUser updateUser,String accessToken) {
        return given().log().all()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .body(updateUser)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }

}
