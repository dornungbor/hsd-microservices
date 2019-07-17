package com.dornu.hsd.server;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class HsdCrypto {

    public TextEncryptor getTextEncryptor(String password, String salt) {
        return Encryptors.text(password, salt);
    }

}
