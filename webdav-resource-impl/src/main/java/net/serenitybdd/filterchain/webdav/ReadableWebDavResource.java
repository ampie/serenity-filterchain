package net.serenitybdd.filterchain.webdav;

import com.sbg.bdd.resource.ReadableResource;
import com.sbg.bdd.resource.WritableResource;
import com.sbg.bdd.resource.file.ReadableFileResource;

import java.io.IOException;

public class ReadableWebDavResource extends WebDavResource implements ReadableResource {
    public ReadableWebDavResource(WebDavFolder container, String name) {
        super(container, name);
    }

    @Override
    public byte[] read() {
        try {
            return ReadableFileResource.toBytes(getRoot().getSardine().get(getWebDavUrl()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public WritableWebDavResource asWritable() {
        return new WritableWebDavResource(getContainer(),getName(),true);
    }

    @Override
    public boolean canWrite() {
        return true;
    }
}
