package com.uzumaki.jupiter.service;

import com.uzumaki.jupiter.domain.Category;
import com.uzumaki.jupiter.domain.CategoryRepository;
import com.uzumaki.jupiter.domain.Item;
import com.uzumaki.jupiter.external.TicketMasterAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class RecommendServiceImpl implements RecommendService {

    private ItemService itemService;
    private UserService userService;
    private CategoryRepository categoryRepository;

    @Autowired
    public RecommendServiceImpl(ItemService itemService, UserService userService, CategoryRepository categoryRepository) {
        this.itemService = itemService;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public List<Item> recommendItems(String userId, double lat, double lon) {
        List<Item> recommendedItems = new ArrayList<>();
        Set<Item> favoriteItems = userService.getFavoriteItems(userId);

        Map<String, Integer> allCategories = new HashMap<>();
        for (Item item : favoriteItems) {
            Set<Category> categories = item.getCategories();
            for (Category category : categories) {
                allCategories.put(category.getValue(), allCategories.getOrDefault(category, 0) + 1);
            }
        }
        List<Map.Entry<String, Integer>> categoryList = new ArrayList<>(allCategories.entrySet());
        Collections.sort(categoryList, (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) -> {
            return Integer.compare(e2.getValue(), e1.getValue());
        });

        Set<Item> visitedItemIds = new HashSet<>();
        for (Map.Entry<String, Integer> category : categoryList) {
            List<Item> items = searchItems(lat, lon, category.getKey());
            for (Item item : items) {
                if (!favoriteItems.contains(item) && visitedItemIds.add(item)) {
                    recommendedItems.add(item);
                }
            }
        }
        return recommendedItems;
    }

    @Transactional
    @Override
    public List<Item> searchItems(double lat, double lon, String term) {
        TicketMasterAPI api = new TicketMasterAPI();
        List<Item> items = api.search(lat, lon, term);
        for (Item item : items) {
            itemService.saveItem(item);
        }
        return items;
    }
}
