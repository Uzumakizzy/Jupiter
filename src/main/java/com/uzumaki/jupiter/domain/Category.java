package com.uzumaki.jupiter.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private long id;

    private String value;

    @ManyToMany(mappedBy = "categories")
    private Set<Item> item = new HashSet<>();

    public Category() {
    }

    public long getId() {
        return id;
    }

    public Category setId(long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Category setValue(String value) {
        this.value = value;
        return this;
    }

    public Category setItem(Set<Item> item) {
        this.item = item;
        return this;
    }
}
