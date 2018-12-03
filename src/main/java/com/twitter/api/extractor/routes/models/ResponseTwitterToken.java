package com.twitter.api.extractor.routes.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public final class ResponseTwitterToken implements Serializable {

    private static final Long serialVersionUID = 1L;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    public String getAuthorization() {
        return String.format("%s %s", tokenType, accessToken);
    }

    @Override
    public String toString() {
        return "ResponseTwitterToken{" +
                "tokenType='" + tokenType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}