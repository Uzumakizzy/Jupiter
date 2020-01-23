package com.uzumaki.jupiter.service;

import com.uzumaki.jupiter.domain.Item;
import com.uzumaki.jupiter.domain.User;
import com.uzumaki.jupiter.domain.UserRepository;
import com.uzumaki.jupiter.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean loginUser(String userId, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(userId, password);
        return user.isPresent();
    }

    @Transactional
    @Override
    public void setFavoriteItems(String userId, List<Item> items) {
        Set<Item> favoriteItems = getFavoriteItems(userId);
        favoriteItems.addAll(items);
    }

    @Transactional
    @Override
    public void unsetFavoriteItems(String userId, List<Item> items) {
        Set<Item> favoriteItems = getFavoriteItems(userId);
        favoriteItems.removeAll(items);
    }

    @Transactional
    @Override
    public Set<Item> getFavoriteItems(String userId) {
        Optional<User> user = userRepository.findByUsername(userId);
        if (!user.isPresent()) {
            throw new UserNotFoundException("UserId may be wrong, please try another one.");
        }
        return user.get().getFavoriteItems();
    }

    @Transactional
    @Override
    public String getFullName(String userId) {
        Optional<User> user = userRepository.findByUsername(userId);
        if (!user.isPresent()) {
            throw new UserNotFoundException("UserId may be wrong, please try another one.");
        }
        return user.get().getFirst_name() + " " + user.get().getLast_name();
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }
}
