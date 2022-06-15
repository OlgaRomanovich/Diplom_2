package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private UserClient userClient;
    User user;
    public boolean responseText;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);
    }

    @Test
    @DisplayName("Login with correct credentials")
    public void courierCanLoginWithValidCredentials() {
        UserCredentials userCredentials = new UserCredentials(user.email, user.password);
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int statusCode = loginResponse.extract().statusCode();
        responseText = loginResponse.extract().path("success");
        String accessToken = loginResponse.extract().path("accessToken");
        assertThat("User cannot login", statusCode, equalTo(SC_OK));
        assertThat("ResponseText is incorrect", responseText, equalTo(true));
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Login with empty password")
    public void courierCanLoginWithEmptyPassword() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.email, ""));
        int statusCode = loginResponse.extract().statusCode();
        String responseText = loginResponse.extract().path("message");
        assertThat("Courier cannot login", statusCode, equalTo(401));
        assertThat("Response is correct", responseText, equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login with Invalid Credentials")
    public void courierCanLoginWithInvalidCredentials() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.email, "123456789"));
        int statusCode = loginResponse.extract().statusCode();
        String responseText = loginResponse.extract().path("message");
        assertThat("Courier cannot login", statusCode, equalTo(401));
        assertThat("Response is correct", responseText, equalTo("email or password are incorrect"));
    }
}

