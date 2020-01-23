package com.uzumaki.jupiter.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {

    @Id
    @GeneratedValue
    private long id;
    private String itemId;
    private String name;
    private double rating;
    private String address;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Category> categories = new HashSet<>();
    @ManyToMany(mappedBy = "favoriteItems")
    private Set<User> users = new HashSet<>();
    private String imageUrl;
    private String url;
    private double distance;

    public Item() {
    }

    private Item(ItemBuilder itemBuilder) {
        this.itemId = itemBuilder.itemId;
        this.name = itemBuilder.name;
        this.rating = itemBuilder.rating;
        this.address = itemBuilder.address;
        this.categories = itemBuilder.categories;
        this.imageUrl = itemBuilder.imageUrl;
        this.url = itemBuilder.url;
        this.distance = itemBuilder.distance;
    }

    public String getItem_id() {
        return itemId;
    }

    public long getId() {
        return id;
    }

    public Item setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Set<User> getUsers() {
        return users;
    }

    public double getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Set<String> getCategoriesValue() {
        Set<String> values = new HashSet<>();
        for (Category category : categories) {
            values.add(category.getValue());
        }
        return values;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getDistance() {
        return distance;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("item_id", itemId);
            obj.put("name", name);
            obj.put("rating", rating);
            obj.put("address", address);
            obj.put("categories", new JSONArray(getCategoriesValue()));
            obj.put("image_url", imageUrl);
            obj.put("url", url);
            obj.put("distance", distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static class ItemBuilder {
        private String itemId;
        private String name;
        private double rating;
        private String address;
        private Set<Category> categories;
        private String imageUrl;
        private String url;
        private double distance;

        public ItemBuilder setId(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public ItemBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public ItemBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ItemBuilder setRating(double rating) {
            this.rating = rating;
            return this;
        }

        public ItemBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public ItemBuilder setCategories(Set<Category> categories) {
            this.categories = categories;
            return this;
        }

        public ItemBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ItemBuilder setDistance(double distance) {
            this.distance = distance;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }
}
