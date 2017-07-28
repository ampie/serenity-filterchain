package net.serenitybdd.cucumber.filterchain.publishers;

import com.sbg.bdd.resource.ResourceRoot;
import net.serenitybdd.filterchain.webdav.WebDavConfig;
import net.serenitybdd.filterchain.webdav.WebDavRoot;

import java.io.File;
import java.io.IOException;

public class WebDavPublishingStrategy extends AbstractResourcePublishingStrategy {
    private String username;
    private String password;
    private WebDavRoot webdav;

    @Override
    public void copy(File source, String destination) {
        super.copy(source, destination);
        try {
            webdav.getSardine().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        webdav = new WebDavRoot("webdav", new WebDavConfig(destination, username, password));
        return webdav;
    }
}
