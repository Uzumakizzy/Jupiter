package com.uzumaki.jupiter.external;

import com.uzumaki.jupiter.Utils.GeoHash;
import com.uzumaki.jupiter.domain.Category;
import com.uzumaki.jupiter.domain.Item;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicketMasterAPI {

    private static final String URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    private static final String DEFAULT_KEYWORD = ""; // no restriction
    private static final String API_KEY = "d2qrdf2K0SHF4s4BIt0KGZEhalsEpAkG";

    public List<Item> search(double lat, double lon, String keyword) {
        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }

        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String geoHash = GeoHash.encodeGeoHash(lat, lon, 8);

        String query = String.format("apikey=%s&geoPoint=%s&keyword=%s&radius=%s", API_KEY, geoHash, keyword, 50);

        String url = URL + "?" + query;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode(); // get data in this step

            System.out.println("send request to uri: " + url);
            System.out.println(responseCode);

            if (responseCode != 200) {
                System.out.println("error status code is " + responseCode);
                return new ArrayList<Item>();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();

            JSONObject object = new JSONObject(builder.toString());

            if (!object.isNull("_embedded")) {
                JSONObject embedded = object.getJSONObject("_embedded");
                return getItemList(embedded.getJSONArray("events"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Item>();
    }

    private List<Item> getItemList(JSONArray events) throws JSONException {
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < events.length(); ++i) {
            JSONObject event = events.getJSONObject(i);

            Item.ItemBuilder builder = new Item.ItemBuilder();

            if (!event.isNull("id")) {
                builder.setId(event.getString("id"));
            }

            if (!event.isNull("name")) {
                builder.setName(event.getString("name"));
            }

            if (!event.isNull("url")) {
                builder.setUrl(event.getString("url"));
            }

            if (!event.isNull("distance")) {
                builder.setDistance(event.getDouble("distance"));
            }

            if (!event.isNull("rating")) {
                builder.setRating(event.getDouble("rating"));
            }

            builder.setAddress(getAddress(event));
            builder.setImageUrl(getImageUrl(event));
            builder.setCategories(getCategories(event));
            items.add(builder.build());
        }
        return items;
    }

    private String getAddress(JSONObject event) throws JSONException {
        if (!event.isNull("_embedded")) {
            JSONObject embedded = event.getJSONObject("_embedded");
            if (!embedded.isNull("venues")) {
                JSONArray venues = embedded.getJSONArray("venues");
                for (int i = 0; i < venues.length(); ++i) {
                    JSONObject venue = venues.getJSONObject(i);
                    StringBuilder builder = new StringBuilder();
                    if (!venue.isNull("address")) {
                        JSONObject address = venue.getJSONObject("address");
                        if (!address.isNull("line1")) {
                            builder.append(address.getString("line1"));
                        }
                        if (!address.isNull("line2")) {
                            builder.append(",");
                            builder.append(address.getString("line2"));
                        }
                        if (!address.isNull("line3")) {
                            builder.append(",");
                            builder.append(address.getString("line3"));
                        }
                    }

                    if (!venue.isNull("city")) {
                        JSONObject city = venue.getJSONObject("city");
                        if (!city.isNull("name")) {
                            builder.append(",");
                            builder.append(city.getString("name"));
                        }
                    }

                    String addressStr = builder.toString();
                    if (!addressStr.isEmpty()) {
                        return addressStr;
                    }
                }
            }
        }
        return "";

    }

    private String getImageUrl(JSONObject event) throws JSONException {
        if (!event.isNull("images")) {
            JSONArray array = event.getJSONArray("images");
            for (int i = 0; i < array.length(); i++) {
                JSONObject image = array.getJSONObject(i);
                if (!image.isNull("url")) {
                    return image.getString("url");
                }
            }
        }
        return "";
    }

    private Set<Category> getCategories(JSONObject event) throws JSONException {
        Set<Category> categories = new HashSet<>();
        if (!event.isNull("classifications")) {
            JSONArray classifications = event.getJSONArray("classifications");
            for (int i = 0; i < classifications.length(); ++i) {
                JSONObject classification = classifications.getJSONObject(i);
                if (!classification.isNull("segment")) {
                    JSONObject segment = classification.getJSONObject("segment");
                    if (!segment.isNull("name")) {
                        categories.add(new Category().setValue(segment.getString("name")));
                    }
                }
            }

        }
        return categories;
    }
}
