package com.twitter.api.extractor.routes.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class Tweets {

    @JsonProperty("statuses")
    private List<Tweet> tweets;

    public Boolean isEmpty() {
        return tweets.isEmpty();
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    @Override
    public String toString() {
        return "Tweets{" +
                "tweets=" + tweets +
                '}';
    }
}