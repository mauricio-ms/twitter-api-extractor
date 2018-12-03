package com.twitter.api.extractor.routes.beans;

import com.twitter.api.extractor.config.TwitterConfig;
import com.twitter.api.extractor.routes.helpers.DateHelper;
import com.twitter.api.extractor.routes.helpers.TwitterHelper;
import com.twitter.api.extractor.routes.models.ResponseTwitterToken;
import com.twitter.api.extractor.routes.models.Tweets;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.PriorityQueue;

@Component(value = SearchOldTweetsBean.NAME)
public class SearchOldTweetsBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchOldTweetsBean.class);

    public static final String NAME = "searchOldTweetsBean";

    @Value("${twitter.search.keywords}")
    private String keywords;

    @Value("${twitter.search.until}")
    private String until;

    @Autowired
    private TwitterConfig twitterConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DateHelper dateHelper;

    @Autowired
    private TwitterHelper twitterHelper;

    private Integer daysRead = 0;

    private Long lowestTweetId;

    public void search(final Exchange exchange) {
        LOGGER.info("Search older tweets: {}", exchange);

        try {
            final String jsonTweets = searchTweets();
            exchange.getIn()
                    .setBody(new JSONObject(jsonTweets).get("statuses").toString());

            final ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final Tweets tweets = objectMapper.readValue(jsonTweets, Tweets.class);
            LOGGER.info("Tweets found: {}", jsonTweets);
            LOGGER.info("Tweets size: {}", tweets.getTweets().size());

            if (tweets.isEmpty()) {
                daysRead++;
                lowestTweetId = null;
            } else {
                lowestTweetId = new PriorityQueue<>(tweets.getTweets()).poll().getId();
            }

            LOGGER.debug("LowestTweetId: {}", lowestTweetId);
        } catch (IOException e) {
            final String msg = "Error searching tweets";
            LOGGER.error(msg, e);
            throw new RuntimeException(msg, e);
        } catch (JSONException e) {
            final String msg = "Error parsing json";
            LOGGER.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private ResponseTwitterToken getTwitterToken() {
        LOGGER.info("Get Twitter Token");
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(
                MediaType.APPLICATION_JSON
        ));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        final String tokenBase64 = Base64.getEncoder().encodeToString(
                String.format("%s:%s", twitterConfig.getKey(), twitterConfig.getSecret())
                        .getBytes());
        headers.add("Authorization", String.format("Basic %s", tokenBase64));
        final MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "client_credentials");
        final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(data, headers);
        return restTemplate.postForEntity(twitterConfig.getTokenUrl(), httpEntity, ResponseTwitterToken.class)
                .getBody();
    }

    private String searchTweets() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getTwitterToken().getAuthorization());

        final String params = Optional.ofNullable(lowestTweetId)
                .map(v -> String.format("max_id=%s&", v - 1))
                .orElse("since_id=0&")
                .concat(String.format("q=%s&result_type=recent&tweet_mode=extended&count=30&until=%s",
                        twitterHelper.getQueryAsOr(keywords), dateHelper.getDatePlusDays(until, daysRead)));
        final String searchTweetsUrl = String.format("%s?%s", twitterConfig.getSearchTweetsUrl(), params);

        LOGGER.info("Search Tweets Url: {}", searchTweetsUrl);
        return restTemplate.exchange(
                searchTweetsUrl, HttpMethod.GET,
                new HttpEntity<>(headers), String.class
        ).getBody();
    }
}