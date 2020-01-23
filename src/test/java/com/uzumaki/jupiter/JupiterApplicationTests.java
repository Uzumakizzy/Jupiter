package com.uzumaki.jupiter;

import com.uzumaki.jupiter.domain.Category;
import com.uzumaki.jupiter.domain.CategoryRepository;
import com.uzumaki.jupiter.domain.Item;
import com.uzumaki.jupiter.domain.ItemRepository;
import com.uzumaki.jupiter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class JupiterApplicationTests {

    private ItemRepository itemRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public JupiterApplicationTests(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void addCategory() {
        Set<Category> set = new HashSet<>();
        set.add(new Category().setValue("music"));
        set.add(new Category().setValue("sport"));
        System.out.println(itemRepository.save(new Item.ItemBuilder().setAddress("1").setCategories(set).setDistance(12).
                setId("123").setImageUrl("qedfd").setName("erwe").setRating(32).setUrl("dfdf").build()));
    }

}
