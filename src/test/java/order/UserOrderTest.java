package order;

import user.User;
import user.UserClient;
import user.UserCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserOrderTest {
    User user;
    String ingredientHash;

    @Before
    public void setUp() {
        UserClient userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);
    }

    @Test
    @Description("Body returns orders list correctly with authorization")
    @DisplayName("Order List")
    public void bodyReturnsOrdersListCorrectly() {
        UserClient userClient = new UserClient();
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.email, user.password));
        String accessToken = loginResponse.extract().path("accessToken");
        OrderClient orderClient = new OrderClient();
        Order order = new Order(new String[]{ingredientHash});
        ValidatableResponse createOrderResponse = orderClient.createOrder(order, accessToken);
        List<String> orderListCheck = createOrderResponse.extract().path("orders");
        assertThat("Order List is null", orderListCheck, is(not(empty())));
    }

    @Test
    @Description("Body returns orders list correctly without authorization")
    @DisplayName("Order List")
    public void bodyReturnsOrdersListWithoutAuthorization() {
        String accessToken = "wertyxcvghjk";
        OrderClient orderClient = new OrderClient();
        Order order = new Order(new String[]{ingredientHash});
        ValidatableResponse createOrderResponse = orderClient.createOrder(order, accessToken);
        List<String> orderListCheck = createOrderResponse.extract().path("orders");
        assertThat("Order List is null", orderListCheck, is(not(empty())));
    }
}
