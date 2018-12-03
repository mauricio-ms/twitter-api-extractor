package com.twitter.api.extractor.routes.helpers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TwitterHelper {

    public String getQueryAsOr(final String text) {
        return StringUtils.replace(text, ",", " OR ");
    }
}