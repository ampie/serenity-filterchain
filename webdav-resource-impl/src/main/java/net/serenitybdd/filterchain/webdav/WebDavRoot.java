package net.serenitybdd.filterchain.webdav;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.sbg.bdd.resource.ResourceRoot;

public class WebDavRoot extends WebDavFolder implements ResourceRoot {
    private final String rootName;
    private WebDavConfig config;
    private Sardine sardine;

    public WebDavRoot(String rootName,WebDavConfig config) {
        super(null, rootName);
        this.rootName = rootName;
        this.config = config;
        this.sardine = SardineFactory.begin(config.getUsername(), config.getPassword());

    }
    public String getWebDavUrl(){
        String baseUrl = config.getBaseUrl();
        if(baseUrl.endsWith("/")){
            return baseUrl;
        }
        return baseUrl + "/";
    }
    public Sardine getSardine() {
        return sardine;
    }
    protected boolean isNotMe(DavResource davResource) {
        return !config.getBaseUrl().endsWith(davResource.getPath());
    }


    @Override
    public String getPath() {
        return "";
    }

    @Override
    public WebDavRoot getRoot() {
        return this;
    }

    @Override
    public String getRootName() {
        return rootName;
    }
}
