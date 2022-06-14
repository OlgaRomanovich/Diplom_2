package USER;

public class UpdateUser {
    public String email;
    public String password;
    public String name;

    public UpdateUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}