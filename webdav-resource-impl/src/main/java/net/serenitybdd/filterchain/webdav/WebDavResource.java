package net.serenitybdd.filterchain.webdav;

import com.sbg.bdd.resource.Resource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebDavResource implements Resource {

    private WebDavFolder container;
    private String name;

    public WebDavResource(WebDavFolder container, String name) {
        this.container = container;
        this.name = name;
    }

    @Override
    public String getPath() {
        if (getContainer() instanceof WebDavRoot) {
            return getName();
        } else {
            return getContainer().getPath() + "/" + getName();
        }
    }

    public String getEncodedPath() {
        if (getContainer() instanceof WebDavRoot) {
            return getEncodedName();
        } else {
            return getContainer().getEncodedPath() + "/" + getEncodedName();
        }
    }

    private String getEncodedName() {
        try {
            String encode = URLEncoder.encode(getName(), "UTF-8");
            return encode;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getWebDavUrl() {
        String encode = getRoot().getWebDavUrl() + getEncodedPath();
        return encode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public WebDavRoot getRoot() {
        return getContainer().getRoot();
    }

    @Override
    public WebDavFolder getContainer() {
        return container;
    }

    public boolean exists() {
        try {
            return getRoot().getSardine().exists(getWebDavUrl());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
