package com.example.demo.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity
public class Group {

    @Id
    private ObjectId id;

    private String name;
}
