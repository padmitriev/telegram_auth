package dpa.testwork.tlgrm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TempController {

    @GetMapping("/")
    public String showProfile(@RequestParam String initData, Model model) {
        // заглушка
        Map<String, Object> userData = parseInitData(initData);

        model.addAttribute("id", userData.get("id"));
        model.addAttribute("firstName", userData.get("first_name"));
        model.addAttribute("lastName", userData.get("last_name"));
        model.addAttribute("username", userData.get("username"));

        return "profile";
    }

    // заглушка
    private Map<String, Object> parseInitData(String initData) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", 123456789L);
        user.put("first_name", "Иван");
        user.put("last_name", "Телеграмов");
        user.put("username", "ivan_telegramov");
        return user;
    }



    @GetMapping("/status")
    public String checkStatus() {
        return "Server is running on port 8082";
    }
}