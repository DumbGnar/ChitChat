package com.example.demo.dao;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class SingleDatastore {

    private static Datastore datastore = null;

    public static Datastore getInstance() {
        if (datastore == null) {
            datastore = Morphia.createDatastore(MongoClients.create(
                    "mongodb://129.211.62.153:27022"), "chitchat");
            datastore.getMapper().mapPackage("com.example.demo.model");
            datastore.ensureIndexes();
        }
        return datastore;
    }
}
