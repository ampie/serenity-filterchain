package net.serenitybdd.cucumber.filterchain.publishers;

import com.sbg.bdd.resource.ResourceRoot;
import net.serenitybdd.filterchain.webdav.WebDavConfig;
import net.serenitybdd.filterchain.webdav.WebDavRoot;

public class WebDavPublishingStrategy extends AbstractResourcePublishingStrategy {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected ResourceRoot getDestination(String destination) {
        return new WebDavRoot("webdav",new WebDavConfig(destination, username, password));
    }
}
