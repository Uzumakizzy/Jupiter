package com.uzumaki.jupiter.Utils;

import com.uzumaki.jupiter.domain.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ControllerHelper {

    // Parses a JSONObject from http request.
    public static JSONObject readJSONObject(HttpServletRequest request) {
        StringBuilder sBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sBuilder.append(line);
            }
            return new JSONObject(sBuilder.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    // Write a JSONArray to http response
    public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*"); // 权限控制 "*"为通配符，表示任意用户都可以访问
        PrintWriter out = response.getWriter();
        out.print(array);
        out.close();
    }

    // Write a JSONObject to http response
    public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*"); // 权限控制 "*"为通配符，表示任意用户都可以访问
        PrintWriter out = response.getWriter();
        out.print(obj);
        out.close();
    }

    // Converts a list of Item objects to JSONArray.
    public static JSONArray getJSONArray(List<Item> items) {
        JSONArray result = new JSONArray();
        try {
            for (Item item : items) {
                result.put(item.toJSONObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
