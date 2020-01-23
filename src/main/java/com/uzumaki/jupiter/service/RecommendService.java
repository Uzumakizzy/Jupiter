package com.uzumaki.jupiter.service;

import com.uzumaki.jupiter.domain.Item;

import java.util.List;

public interface RecommendService {

    List<Item> recommendItems(String userId, double lat, double lon);

    List<Item> searchItems(double lat, double lon, String term);
}
