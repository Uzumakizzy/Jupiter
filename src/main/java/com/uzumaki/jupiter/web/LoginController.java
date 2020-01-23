package com.uzumaki.jupiter.web;

import com.uzumaki.jupiter.Utils.ControllerHelper;
import com.uzumaki.jupiter.domain.User;
import com.uzumaki.jupiter.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/register")
    public void register(HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject input = ControllerHelper.readJSONObject(request);
            String userId = input.getString("user_id");
            String password = input.getString("password");
            String first_name = input.getString("first_name");
            String last_name = input.getString("last_name");

            JSONObject obj = new JSONObject();
            if (userService.registerUser(new User().setUsername(userId).setPassword(password).
                    setFirst_name(first_name).setLast_name(last_name)) != null) {
                obj.put("status", "OK");
            } else {
                obj.put("status", "User already exists");
            }
            ControllerHelper.writeJsonObject(response, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/login")
    public void loginPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject input = ControllerHelper.readJSONObject(request);
            String userId = input.getString("user_id");
            String password = input.getString("password");

            JSONObject obj = new JSONObject();
            if (userService.loginUser(userId, password)) {
                HttpSession session = request.getSession();
                session.setAttribute("user_id", userId);
                session.setMaxInactiveInterval(600);
                obj.put("status", "OK").put("user_id", userId).put("name", userService.getFullName(userId));
            } else {
                response.setStatus(401);
                obj.put("status", "User Doesn't Exists");
            }
            ControllerHelper.writeJsonObject(response, obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/login")
    public void loginGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);

            JSONObject obj = new JSONObject();
            if (session != null) {
                String userId = session.getAttribute("user_id").toString();
                obj.put("status", "OK").put("user_id", userId).put("name", userService.getFullName(userId));
            } else {
                response.setStatus(403);
                obj.put("status", "Session Invalid");
            }
            ControllerHelper.writeJsonObject(response, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("/Jupiter");
    }
}
