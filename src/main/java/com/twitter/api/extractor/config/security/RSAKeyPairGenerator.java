package com.twitter.api.extractor.config.security;

import com.twitter.api.extractor.routes.helpers.PathsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

@Component
public class RSAKeyPairGenerator {

    private final PathsHelper pathsHelper;

    @Autowired
    public RSAKeyPairGenerator(final PathsHelper pathsHelper) {
        this.pathsHelper = pathsHelper;
    }

    public void generate() throws URISyntaxException,
            IOException, NoSuchAlgorithmException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        final KeyPair pair = keyGen.generateKeyPair();
        final PrivateKey privateKey = pair.getPrivate();
        final PublicKey publicKey = pair.getPublic();
        final String securityPath = String.valueOf(pathsHelper
                .getResource("security"));
        writeToFile(String.format("%s/public", securityPath),
                publicKey.getEncoded());
        writeToFile(String.format("%s/private", securityPath),
                privateKey.getEncoded());
    }

    private void writeToFile(final String path,
                             final byte[] key) throws IOException {
        Files.write(Paths.get(path), key);
    }
}