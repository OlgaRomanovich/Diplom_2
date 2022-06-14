package USER;

import org.apache.commons.lang3.RandomStringUtils;

public class User {
    public String email;
    public String password;
    public String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }


    public static User getRandomUser() {
        final String userName = RandomStringUtils.randomAlphabetic(10);
        final String userEmail = RandomStringUtils.randomAlphabetic(5) + "@" + RandomStringUtils.randomAlphabetic(5) + ".ru";
        final String userPassword = RandomStringUtils.randomAlphabetic(10);
        return new User(userEmail, userPassword, userName);
    }
}
