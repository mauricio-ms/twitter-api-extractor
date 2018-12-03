package com.twitter.api.extractor.config.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@RunWith(MockitoJUnitRunner.class)
public class RSAHelperTest {

    private RSAHelper rsaHelper;

    @Mock
    private RSAKeyPairGenerator rsaKeyPairGenerator;

    @Before
    public void setUp() throws NoSuchAlgorithmException, IOException, URISyntaxException {
        rsaHelper = new RSAHelper(rsaKeyPairGenerator);
    }

    @Test
    public void passwordAfterDecryptShouldBeTheSameAsBefore() throws Exception {
        final String password = "somePasswordToUnitTest";
        final String encrypted = Base64.getEncoder()
                .encodeToString(rsaHelper.encrypt(password));
        final String decrypted = rsaHelper.decrypt(encrypted);
        Assert.assertEquals(password, decrypted);
    }
}