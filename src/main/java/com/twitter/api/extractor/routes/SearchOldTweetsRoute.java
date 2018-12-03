package com.twitter.api.extractor.routes;

import com.twitter.api.extractor.config.MongoDbConfig;
import com.twitter.api.extractor.routes.beans.SearchOldTweetsBean;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchOldTweetsRoute extends AbstractTweetsRoute {

    @Autowired
    private MongoDbConfig mongoDbConfig;

    @Override
    public void configure() throws Exception {
        configureDelayWhenRateLimitExceeded();
        from("timer://timerSearch?period=2000")
                .setBody()
                .simple("Timer search fired at ${header.firedTime}")
                .toF("bean:%s?method=search", SearchOldTweetsBean.NAME)
        .to(mongoDbConfig.getMongoDbUriInsert());
    }
}