package net.serenitybdd.filterchain.webdav;

import com.sbg.bdd.resource.WritableResource;

import java.io.IOException;

public class WritableWebDavResource extends WebDavResource implements WritableResource {
    private final boolean canRead;

    public WritableWebDavResource(WebDavFolder container, String name, boolean canRead) {
        super(container, name);
        this.canRead = canRead;
    }

    @Override
    public void write(byte[] data) {
        try {
            if(!exists()){
                getContainer().clearCachedChildren();
            }
            getContainer().mkdirs();
            getRoot().getSardine().put(getWebDavUrl(), data);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean canRead() {
        return canRead;
    }

    @Override
    public ReadableWebDavResource asReadable() {
        return new ReadableWebDavResource(getContainer(), getName());
    }
}
