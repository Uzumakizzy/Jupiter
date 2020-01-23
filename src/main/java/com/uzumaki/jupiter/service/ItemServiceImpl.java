package com.uzumaki.jupiter.service;

import com.uzumaki.jupiter.domain.Category;
import com.uzumaki.jupiter.domain.Item;
import com.uzumaki.jupiter.domain.ItemRepository;
import com.uzumaki.jupiter.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    @Override
    public Set<Category> getCategories(String itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (!item.isPresent()) {
            throw new ItemNotFoundException("itemId may be wrong, please try another one.");
        }
        return item.get().getCategories();
    }

    @Transactional
    @Override
    public Item getItemById(String itemId) {
        Optional<Item> item = itemRepository.findByItemId(itemId);
        if (!item.isPresent()) {
            throw new ItemNotFoundException("itemId may be wrong, please try another one.");
        }
        return item.get();
    }


    @Transactional
    @Override
    public Set<Item> getItemsByIds(List<String> itemIds) {
        Set<Item> items = new HashSet<>();
        for (String itemId : itemIds) {
            Optional<Item> item = itemRepository.findById(itemId);
            if (!item.isPresent()) {
                throw new ItemNotFoundException("itemId may be wrong, please try another one.");
            }
            items.add(item.get());
        }
        return items;
    }

    @Transactional
    @Override
    public Item saveItem(Item item) {
        Optional<Item> temp = itemRepository.findByItemId(item.getItem_id());
        if (!temp.isPresent()) {
            return itemRepository.save(item);
        }
        return null;
    }

}
