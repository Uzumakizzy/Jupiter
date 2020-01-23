package com.uzumaki.jupiter.service;

import com.uzumaki.jupiter.domain.Item;
import com.uzumaki.jupiter.domain.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    boolean loginUser(String userId, String password);

    void setFavoriteItems(String userId, List<Item> items);

    void unsetFavoriteItems(String userId, List<Item> items);

    Set<Item> getFavoriteItems(String userId);

    String getFullName(String userId);

    User registerUser(User user);
}
