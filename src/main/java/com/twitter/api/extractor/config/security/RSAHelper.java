package com.twitter.api.extractor.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

@Component
public class RSAHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAHelper.class);

    @Autowired
    public RSAHelper(final RSAKeyPairGenerator rsaKeyPairGenerator) throws NoSuchAlgorithmException, IOException, URISyntaxException {
        rsaKeyPairGenerator.generate();
    }

    public byte[] encrypt(final String data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        return cipher.doFinal(data.getBytes());
    }

    public String decrypt(final String data) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()),
                getPrivateKey());
    }

    private String decrypt(final byte[] data,
                          final PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    private PublicKey getPublicKey() {
        try {
            final String publicKey = getKey("security/public_key.txt");
            final EncodedKeySpec keySpec = new X509EncodedKeySpec(
                    Base64.getDecoder().decode(publicKey.getBytes()));
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            LOGGER.error("Error obtaining the public key", e);
            return null;
        }
    }

    private PrivateKey getPrivateKey() {
        try {
            final String privateKey = getKey("security/private_key.txt");
            final EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
                    Base64.getDecoder().decode(privateKey.getBytes()));
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            LOGGER.error("Error obtaining the private key", e);
            return null;
        }
    }

    private String getKey(final String pathKey) throws IOException, URISyntaxException {
        return Files.lines(
                Paths.get(RSAHelper.class.getClassLoader().getResource(pathKey).toURI())
        ).collect(Collectors.joining());
    }
}