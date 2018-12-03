package com.twitter.api.extractor.routes.helpers;

import org.junit.Assert;
import org.junit.Test;

public class TwitterHelperTest {

    private TwitterHelper twitterHelper = new TwitterHelper();

    @Test
    public void getQueryAsOrAssert() {
        final String queryAsOr = twitterHelper.
                getQueryAsOr("java,scala");
        Assert.assertEquals("java OR scala", queryAsOr);
    }
}