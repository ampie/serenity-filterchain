package net.serenitybdd.filterchain.webdav;


import com.github.sardine.DavResource;
import com.sbg.bdd.resource.*;
import com.sbg.bdd.resource.file.FileSystemResource;
import com.sbg.bdd.resource.file.ReadableFileResource;
import com.sbg.bdd.resource.file.WritableFileResource;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebDavFolder extends WebDavResource implements ResourceContainer {
    static Logger LOGGER = Logger.getLogger(WebDavFolder.class.getName());
    private Map<String, WebDavResource> children;

    public WebDavFolder(WebDavFolder container, String name) {
        super(container, name);
    }

    @Override
    public WebDavResource[] list() {
        return getChildren().values().toArray(new WebDavResource[getChildren().size()]);
    }

    @Override
    public Resource[] list(ResourceFilter filter) {
        return ResourceSupport.list(filter, getChildren(), this);
    }

    @Override
    public WebDavResource resolveOrFail(String... segments) throws IllegalArgumentException {
        return ResourceSupport.resolveExisting(this, segments, true);
    }

    @Override
    public WebDavResource resolveExisting(String... segments) {
        return ResourceSupport.resolveExisting(this, segments, false);
    }

    @Override
    public WebDavFolder resolvePotentialContainer(String... segments) {
        String[] flattened = ResourceSupport.flatten(segments);
        WebDavResource previous = resolvePotential(flattened, flattened.length);
        if (previous instanceof WebDavFolder) {
            return (WebDavFolder) previous;
        } else {
            return null;
        }
    }

    @Override
    public WritableWebDavResource resolvePotential(String... segments) {
        String[] flattened = ResourceSupport.flatten(segments);
        WebDavResource previous = resolvePotential(flattened, flattened.length - 1);
        if (previous instanceof WritableWebDavResource) {
            return (WritableWebDavResource) previous;
        } else if (previous instanceof ReadableWebDavResource) {
            return ((ReadableWebDavResource) previous).asWritable();
        } else {
            return null;
        }
    }

    private WebDavResource resolvePotential(String[] flattened, int lastDirectoryIndex) {
        WebDavResource previous = this;
        for (int i = 0; i < flattened.length; i++) {
            if (previous instanceof WebDavFolder) {
                WebDavResource child = ((WebDavFolder) previous).getChild(flattened[i]);
                if (child == null) {
                    if (i < lastDirectoryIndex) {
                        child = new WebDavFolder((WebDavFolder) previous, flattened[i]);
                    } else {
                        child = new WritableWebDavResource((WebDavFolder) previous, flattened[i], false);
                    }
                }
                previous = child;
            }
        }
        return previous;
    }

    @Override
    public boolean fallsWithin(String path) {
        return path.startsWith(getPath());
    }

    @Override
    public WebDavResource getChild(String segment) {
        return getChildren().get(segment);
    }

    Map<String, WebDavResource> getChildren() {
        if (children == null) {
            if (exists())
                try {
                    List<DavResource> list = getRoot().getSardine().list(getWebDavUrl());
                    children = new HashMap<>();
                    for (DavResource davResource : list) {
                        String name = URLDecoder.decode(davResource.getName(), "UTF-8");
                        if (davResource.isDirectory()) {
                            if (isNotMe(davResource)) {
                                children.put(name, new WebDavFolder(this, name));
                            }
                        } else {
                            children.put(name, new ReadableWebDavResource(this, name));
                        }
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Could not retrieve children for " + getWebDavUrl(), e);
                    throw new IllegalStateException(e);
                }
            else {
                children = new HashMap<>();
            }
        }
        return children;
    }

    protected boolean isNotMe(DavResource davResource) {
        return !davResource.getPath().endsWith(getPath() + "/");
    }


    public void mkdirs() {
        try {
            if (!exists()) {
                getContainer().mkdirs();
                getContainer().clearCachedChildren();
                getRoot().getSardine().createDirectory(getWebDavUrl());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void clearCachedChildren() {
        children = null;
    }

}
