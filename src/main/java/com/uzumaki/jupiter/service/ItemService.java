package com.uzumaki.jupiter.service;

import com.uzumaki.jupiter.domain.Category;
import com.uzumaki.jupiter.domain.Item;

import java.util.List;
import java.util.Set;

public interface ItemService {

    Set<Category> getCategories(String itemId);

    Item getItemById(String itemId);

    Set<Item> getItemsByIds(List<String> itemIds);

    Item saveItem(Item item);
}
