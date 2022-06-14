package USER;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UpdateUserDataTest {
    User user;
    private UserClient userClient;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.email, user.password));
        String accessToken = loginResponse.extract().path("accessToken");
        userClient.delete(accessToken);
    }


    @Test
    @DisplayName("Update user information")
    public void updateUser() {
        UserCredentials userCredentials = new UserCredentials(user.email, user.password);
        User user2 = User.getRandomUser();
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        String accessToken = loginResponse.extract().path("accessToken");
        ValidatableResponse updateResponse = userClient.update(new UpdateUser(user2.email, user2.password, user2.name), accessToken);
        int statusCode = updateResponse.extract().statusCode();
        boolean responseText = updateResponse.extract().path("success");
        assertThat("Courier is updated", statusCode, equalTo(200));
        assertThat("Response is correct", responseText, equalTo(true));
    }

    @Test
    @DisplayName("Update user email with exists address")
    public void updateUserWithExistsEmail() {
        User user2 = User.getRandomUser();
        userClient.create(user2);
        UserCredentials userCredentials = new UserCredentials(user.email, user.password);
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        String accessToken = loginResponse.extract().path("accessToken");
        ValidatableResponse updateResponse = userClient.update(new UpdateUser(user2.email, user2.password, user2.name), accessToken);
        int statusCode = updateResponse.extract().statusCode();
        String responseText = updateResponse.extract().path("message");
        assertThat("Courier is updated", statusCode, equalTo(403));
        assertThat("Response is correct", responseText, equalTo("User with such email already exists"));
    }


    @Test
    @DisplayName("Update user information without authorization")
    public void updateUserWithoutAuthorization() {
        UserCredentials userCredentials = new UserCredentials(user.email, user.password);
        String accessToken = "qwertyuytrewq";
        ValidatableResponse updateResponse = userClient.update(new UpdateUser(user.email, user.password, user.name), accessToken);
        int statusCode = updateResponse.extract().statusCode();
        String responseText = updateResponse.extract().path("message");
        assertThat("Courier is updated", statusCode, equalTo(401));
        assertThat("Response is correct", responseText, equalTo("You should be authorised"));
    }
}
