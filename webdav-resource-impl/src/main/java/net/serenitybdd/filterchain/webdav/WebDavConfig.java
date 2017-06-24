package net.serenitybdd.filterchain.webdav;

public class WebDavConfig {
    private String baseUrl;
    private String username;
    private String password;

    public WebDavConfig(String baseUrl, String username, String password) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
