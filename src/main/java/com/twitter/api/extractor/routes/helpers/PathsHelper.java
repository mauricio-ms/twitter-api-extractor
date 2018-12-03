package com.twitter.api.extractor.routes.helpers;

import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PathsHelper {

    public Path getResource(final String name) throws URISyntaxException {
        return Paths.get(PathsHelper.class.getClassLoader()
                .getResource(name).toURI());
    }
}
