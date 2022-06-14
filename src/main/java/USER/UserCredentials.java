package USER;

public class UserCredentials {
    private final String email;
    private final String password;
    public UserCredentials (String email, String password) {
        this.email=email;
        this.password=password;
    }
    public UserCredentials from(User user) {
        return new UserCredentials (user.email, user.password);
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}

