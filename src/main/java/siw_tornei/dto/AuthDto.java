package siw_tornei.dto;

public class AuthDto {

    private boolean authenticated;
    private String username;

    public AuthDto() {
    }

    public AuthDto(
            boolean authenticated,
            String username) {

        this.authenticated = authenticated;
        this.username = username;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}