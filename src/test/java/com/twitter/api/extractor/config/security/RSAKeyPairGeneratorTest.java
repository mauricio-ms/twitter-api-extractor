package com.twitter.api.extractor.config.security;

import com.twitter.api.extractor.routes.helpers.PathsHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

@RunWith(MockitoJUnitRunner.class)
public class RSAKeyPairGeneratorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAKeyPairGeneratorTest.class);

    private RSAKeyPairGenerator rsaKeyPairGenerator;

    @Mock
    private PathsHelper pathsHelper;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        rsaKeyPairGenerator = new RSAKeyPairGenerator(pathsHelper);
        try {
            Files.deleteIfExists(new PathsHelper()
                    .getResource("security/private"));
            Files.deleteIfExists(new PathsHelper()
                    .getResource("security/public"));
        } catch(Exception e) {
            LOGGER.error("Directory empty", e);
        }
        Mockito.when(pathsHelper.getResource("security"))
                .thenReturn(new PathsHelper().getResource("security"));
    }

    @Test
    public void assertThatPrivateKeyExistsAfterGenerationKeys()
            throws NoSuchAlgorithmException, IOException, URISyntaxException {
        rsaKeyPairGenerator.generate();
        final boolean exists = Files.exists(new PathsHelper()
                .getResource("security/private"));
        Assert.assertTrue(exists);
    }

    @Test
    public void assertThatPublicKeyExistsAfterGenerationKeys()
            throws NoSuchAlgorithmException, IOException, URISyntaxException {
        rsaKeyPairGenerator.generate();
        final boolean exists = Files.exists(new PathsHelper()
                .getResource("security/public"));
        Assert.assertTrue(exists);
    }
}