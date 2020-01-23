package com.uzumaki.jupiter.web;

import com.uzumaki.jupiter.Utils.ControllerHelper;
import com.uzumaki.jupiter.domain.Item;
import com.uzumaki.jupiter.service.ItemService;
import com.uzumaki.jupiter.service.RecommendService;
import com.uzumaki.jupiter.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ItemController {

    private RecommendService recommendService;
    private UserService userService;
    private ItemService itemService;

    @Autowired
    public ItemController(UserService userService, ItemService itemService, RecommendService recommendService) {
        this.userService = userService;
        this.itemService = itemService;
        this.recommendService = recommendService;
    }

    @GetMapping("/history")
    public void getFavoriteItems(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("user_id");
        JSONArray array = new JSONArray();
        try {
            Set<Item> items = userService.getFavoriteItems(userId);
            for (Item item : items) {
                JSONObject obj = item.toJSONObject();
                obj.put("favorite", true);
                array.put(obj);
            }
            ControllerHelper.writeJsonArray(response, array);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/history")
    public void addFavoriteItems(HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject input = ControllerHelper.readJSONObject(request);
            String userId = input.getString("user_id");
            JSONArray array = input.getJSONArray("favorite");
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                items.add(itemService.getItemById(array.getString(i)));
            }
            userService.setFavoriteItems(userId, items);
            ControllerHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/history")
    public void deleteFavoriteItems(HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject input = ControllerHelper.readJSONObject(request);
            String userId = input.getString("user_id");
            JSONArray array = input.getJSONArray("favorite");
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < array.length(); ++i) {
                items.add(itemService.getItemById(array.getString(i)));
            }
            userService.unsetFavoriteItems(userId, items);
            ControllerHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/recommendation")
    public void getRecommendedItems(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("user_id");
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));
        List<Item> items = recommendService.recommendItems(userId, lat, lon);
        JSONArray array = new JSONArray();
        for (Item item : items) {
            array.put(item.toJSONObject());
        }
        ControllerHelper.writeJsonArray(response, array);
    }

    @GetMapping("/search")
    public void searchItems(HttpServletRequest request, HttpServletResponse response) {
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));
        String userId = request.getParameter("user_id");

        // Term can be empty or null.
        String term = request.getParameter("term");
        try {
            List<Item> items = recommendService.searchItems(lat, lon, term);
            Set<Item> favoriteItems = userService.getFavoriteItems(userId);
            Set<String> itemIds = new HashSet<>();
            for (Item item : favoriteItems) {
                itemIds.add(item.getItem_id());
            }
            JSONArray array = new JSONArray();
            for (Item item : items) {
                JSONObject obj = item.toJSONObject();
                obj.put("favorite", itemIds.contains(item.getItem_id()));
                array.put(obj);
            }
            ControllerHelper.writeJsonArray(response, array);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
