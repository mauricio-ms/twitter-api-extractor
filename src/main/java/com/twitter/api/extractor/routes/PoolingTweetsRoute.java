package com.twitter.api.extractor.routes;

import com.twitter.api.extractor.config.MongoDbConfig;
import com.twitter.api.extractor.config.TwitterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class PoolingTweetsRoute extends AbstractTweetsRoute {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoolingTweetsRoute.class);

    @Value("${twitter.search.keywords}")
    private String keywords;

    @Autowired
    private TwitterConfig twitterConfig;

    @Autowired
    private MongoDbConfig mongoDbConfig;

    @Override
    public void configure() throws Exception {
        configureDelayWhenRateLimitExceeded();

        fromF(buildTwitterSearchUri(), keywords, twitterConfig.getKey(),
                twitterConfig.getSecret(), twitterConfig.getAccessToken(),
                twitterConfig.getAccessTokenSecret())
                .routeId("twitter-search-route")
                .process(exchange -> {
                    LOGGER.info("Reading tweet {}", tweetRead());
                })
                .toF(buildMongoDbUri(), mongoDbConfig.getDbName(),
                        mongoDbConfig.getCollectionName());
    }

    private String buildTwitterSearchUri() {
        return "twitter-search://%s?" +
                "type=polling&delay=10&extendedMode=true&bridgeErrorHandler=true&count=450" +
                "&consumerKey=%s&consumerSecret=%s&accessToken=%s&accessTokenSecret=%s";
    }

    private String buildMongoDbUri() {
        return "mongodb3:mongoBean?database=%s&collection=%s&operation=insert";
    }
}