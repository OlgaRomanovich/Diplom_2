package USER;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    private UserClient userClient;
    User user;
    public boolean responseText;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new User("cucumber22@mail.ru ", "12345", "Ivan");
    }

    @After
    public void tearDown() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.email, user.password));
        String accessToken = loginResponse.extract().path("accessToken");
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Cоздание пользователя с корректными данными")
    public void createCourier() {
        ValidatableResponse createResponse = userClient.create(user);
        int statusCode = createResponse.extract().statusCode();
        responseText = createResponse.extract().path("success");
        assertThat("Courier is created", statusCode, equalTo(200));
        assertThat("Response is correct", responseText, equalTo(true));

    }

    @Test
    @DisplayName("Cоздание пользователя с существующим email")
    public void createUserWithExistsEmail() {
        ValidatableResponse validCourier = userClient.create(user);
        int statusCode1 = validCourier.extract().statusCode();
        assertThat("User is created", statusCode1, equalTo(200));
        User invalidUser = new User("cucumber22@mail.ru", "12345", "Oleg");
        ValidatableResponse createResponse = userClient.create(invalidUser);
        int statusCode = createResponse.extract().statusCode();
        String responseText1 = createResponse.extract().path("message");
        assertThat("Courier is not created", statusCode, equalTo(403));
        assertThat("Response is correct", responseText1, equalTo("User already exists"));
    }

}
