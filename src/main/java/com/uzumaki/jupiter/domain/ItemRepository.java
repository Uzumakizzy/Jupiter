package com.uzumaki.jupiter.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, String> {

    Optional<Item> findById(long id);

    Optional<Item> findByItemId(String itemId);
}
