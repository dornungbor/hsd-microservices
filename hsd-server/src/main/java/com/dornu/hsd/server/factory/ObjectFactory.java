package com.dornu.hsd.server.factory;

import com.dornu.hsd.server.HsdCrypto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Getter
public class ObjectFactory {

    private static ObjectFactory instance;

    @Autowired
    private HsdCrypto crypto;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @PostConstruct
    public void initInstance() {
        instance = this;
    }

    public static ObjectFactory getInstance() {
        return instance;
    }

}
