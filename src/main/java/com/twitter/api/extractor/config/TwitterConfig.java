package com.twitter.api.extractor.config;

import com.twitter.api.extractor.config.security.RSAHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class TwitterConfig {

    private final String tokenUrl;

    private final String searchTweetsUrl;

    private final String key;

    private final String secret;

    private final String accessToken;

    private final String accessTokenSecret;

    @Autowired
    public TwitterConfig(final RSAHelper rsaHelper,
                         @Value("${twitter.config.oauth2.token.url}") final String tokenUrl,
                         @Value("${twitter.config.search.tweets.url}") final String searchTweetsUrl,
                         @Value("${twitter.config.key}") final String key,
                         @Value("${twitter.config.secret}") final String secret,
                         @Value("${twitter.config.access.token}") final String accessToken,
                         @Value("${twitter.config.access.token.secret}") final String accessTokenSecret)
            throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        this.tokenUrl = tokenUrl;
        this.searchTweetsUrl = searchTweetsUrl;
        this.key = rsaHelper.decrypt(key);
        this.secret = rsaHelper.decrypt(secret);
        this.accessToken = rsaHelper.decrypt(accessToken);
        this.accessTokenSecret = rsaHelper.decrypt(accessTokenSecret);
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getSearchTweetsUrl() {
        return searchTweetsUrl;
    }

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }
}