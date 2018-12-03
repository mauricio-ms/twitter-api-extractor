package com.twitter.api.extractor.routes;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractTweetsRoute extends RouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTweetsRoute.class);

    private final AtomicInteger tweetsRead = new AtomicInteger(0);

    protected void configureDelayWhenRateLimitExceeded() {
        onException(Exception.class)
                .process(exchange -> {
                    LOGGER.info("{} tweets read", tweetsRead.get());
                    tweetsRead.set(0);
                })
                .log("Error delivering message")
                .delay(60000 * 15);
    }

    protected int tweetRead() {
        return tweetsRead.incrementAndGet();
    }
}