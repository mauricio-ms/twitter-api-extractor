package com.twitter.api.extractor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDbConfig {

    @Value("${mongo.db.name}")
    private String dbName;

    @Value("${mongo.db.collection.name}")
    private String collectionName;

    @Value("${mongo.db.host}")
    private String host;

    @Value("${mongo.db.port}")
    private Integer port;

    public String getMongoDbUriInsert() {
        return String.format("mongodb3:mongoBean?database=%s&collection=%s&operation=insert",
                dbName, collectionName);
    }

    public String getDbName() {
        return dbName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}