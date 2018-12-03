package com.twitter.api.extractor.routes.models;

public final class Tweet implements Comparable<Tweet> {

    private Long id;

    public Long getId() {
        return id;
    }

    @Override
    public int compareTo(final Tweet other) {
        return id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                '}';
    }
}