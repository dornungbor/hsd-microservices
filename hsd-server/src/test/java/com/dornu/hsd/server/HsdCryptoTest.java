package com.dornu.hsd.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
public class HsdCryptoTest {

    @TestConfiguration
    static class HsdCryptoTestConfiguration {

        @Bean
        public HsdCrypto hsdCrypto() {
            return new HsdCrypto();
        }
    }

    @Autowired
    private HsdCrypto hsdCrypto;

    @Test
    public void when_valid_password_and_salt_then_decrypted_text_is_equal_original_text() {

        String originalText = UUID.randomUUID().toString();
        String salt = KeyGenerators.string().generateKey(), password = "password";

        String encryptedText = hsdCrypto.getTextEncryptor(password, salt).encrypt(originalText);
        String decryptedText = hsdCrypto.getTextEncryptor(password, salt).decrypt(encryptedText);

        assertThat(decryptedText).isEqualTo(originalText);
    }

    @Test(expected = IllegalStateException.class)
    public void when_invalid_password_and_salt_then_expect_IllegalStateException() {

        String originalText = UUID.randomUUID().toString();
        String salt = KeyGenerators.string().generateKey(), password = "password";

        String encryptedText = hsdCrypto.getTextEncryptor(password, salt).encrypt(originalText);
        String decryptedText = hsdCrypto.getTextEncryptor("invalid_password", salt).decrypt(encryptedText);
    }
}
