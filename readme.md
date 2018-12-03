# twitter-api-extractor

Project to extract data of the twitter api by pooling or since a date to now.

Is used two apache camel routes.

* To polling the Apache Camel component of the twitter Api is used
* To extract since a date the bean `SearchOldTweetsBean` contains the implementation
* To write the data, the Apache Camel component of the Mongo Database is used

# Getting Started

## Requirements

* MongoDb instance running
    * The same can be configured in the `application.properties`
        ```
        mongo.db.name=tweetsDb
        mongo.db.collection.name=tweets
        mongo.db.host=127.0.0.1
        mongo.db.port=27017
        ```
* Credentials to use the Twitter API
    * The same can be configured in the `application.properties`
        ```
        twitter.config.key=
        twitter.config.secret=
        twitter.config.access.token=
        twitter.config.access.token.secret=
        ```
        
## Hints

* The keywords to make the search on the Twitter API can be configured in the `application.properties`
    ```
    twitter.search.keywords=keyword1,keyword2,keyword3
    ```