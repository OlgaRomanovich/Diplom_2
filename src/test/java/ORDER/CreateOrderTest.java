package ORDER;

import USER.User;
import USER.UserClient;
import USER.UserCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateOrderTest {
    private OrderClient orderClient;
    UserClient userClient;
    User user;
    String ingredientHash;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);
        orderClient = new OrderClient();
        ValidatableResponse ingredientResponse= orderClient.getIngredients();
        ingredientHash = ingredientResponse.extract().path("data[0]._id");
    }

    @Test
    @Description("Create order without authorization")
    @DisplayName("Create order")
    public void orderCanBeCreatedWithoutAuthorization() {
        Order order = new Order(new String[]{ingredientHash});
        String accessToken= "qwertyui";
        ValidatableResponse createResponse = orderClient.createOrder(order, accessToken);
        boolean response = createResponse.extract().path("success");
        assertThat("Response is not correct", response, equalTo(true));
    }

    @Test
    @Description("Create order with authorization and ingredients")
    @DisplayName("Create order")
    public void orderCanBeCreatedWithValidData() {
        ValidatableResponse loginResponse=userClient.login(new UserCredentials(user.email, user.password));
        String accessToken=loginResponse.extract().path("accessToken");
        Order order = new Order(new String[]{ingredientHash});
        ValidatableResponse createResponse = orderClient.createOrder(order, accessToken);
        boolean response = createResponse.extract().path("success");
        assertThat("Response is not correct", response, equalTo(true));
    }

    @Test
    @Description("Create order with empty ingredients")
    @DisplayName("Create order")
    public void orderCanNotBeCreatedWithEmptyIngredients() {
        Order order = new Order(new String[]{});
        UserCredentials userCredentials = new UserCredentials(user.email, user.password);
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        String accessToken=loginResponse.extract().path("accessToken");
        ValidatableResponse createResponse = orderClient.createOrder(order, accessToken);
        int statusCode = createResponse.extract().statusCode();
        String response = createResponse.extract().path("message");
        assertThat("Order is not created", statusCode, Matchers.equalTo(400));
        assertThat("Response is correct", response, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Create order with wrong hash")
    @DisplayName("Create order")
    public void orderCanNotBeCreatedWithWrongHashOfIngredients() {
        Order order = new Order(new String[]{"123456qwert12345123451q1"});
        UserCredentials userCredentials = new UserCredentials(user.email, user.password);
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        String accessToken=loginResponse.extract().path("accessToken");
        ValidatableResponse createResponse = orderClient.createOrder(order, accessToken);
        int statusCode = createResponse.extract().statusCode();
        assertThat("Order is not created", statusCode, Matchers.equalTo(500));

    }
}
