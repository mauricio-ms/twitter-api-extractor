package com.twitter.api.extractor.config;

import com.twitter.api.extractor.config.security.RSAHelper;
import com.twitter.api.extractor.config.security.RSAKeyPairGenerator;
import com.twitter.api.extractor.routes.PoolingTweetsRoute;
import com.twitter.api.extractor.routes.AbstractTweetsRoute;
import com.twitter.api.extractor.routes.SearchOldTweetsRoute;
import com.twitter.api.extractor.routes.helpers.PathsHelper;
import com.mongodb.MongoClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Configuration
@ComponentScan("br.com.elections.routes")
@Import({TwitterConfig.class, MongoDbConfig.class})
@EnableAutoConfiguration
public class CamelConfig {

    @Autowired
    private MongoDbConfig mongoDbConfig;

    @Value("${twitter.search.until}")
    private String searchUnitl;

    @Bean("mongoBean")
    public MongoClient mongoClient() {
        return new MongoClient(mongoDbConfig.getHost(), mongoDbConfig.getPort());
    }

    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        final MappingJackson2HttpMessageConverter mappingConverter = new MappingJackson2HttpMessageConverter();
        mappingConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.APPLICATION_FORM_URLENCODED));
        restTemplate.getMessageConverters().add(mappingConverter);

        return restTemplate;
    }

    @Bean
    public AbstractTweetsRoute searchTweetsRoute() {
        if(StringUtils.isNotEmpty(searchUnitl)) {
            return new SearchOldTweetsRoute();
        }
        return new PoolingTweetsRoute();
    }

    @Bean
    public PathsHelper pathsHelper() {
        return new PathsHelper();
    }

    @Bean
    public RSAKeyPairGenerator rsaKeyPairGenerator(final PathsHelper pathsHelper) {
        return new RSAKeyPairGenerator(pathsHelper);
    }

    @Bean
    public RSAHelper rsaHelper(final RSAKeyPairGenerator rsaKeyPairGenerator)
            throws NoSuchAlgorithmException, IOException, URISyntaxException {
        return new RSAHelper(rsaKeyPairGenerator);
    }
}