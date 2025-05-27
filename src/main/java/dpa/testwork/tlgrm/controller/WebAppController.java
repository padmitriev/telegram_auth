package dpa.testwork.tlgrm.controller;

import dpa.testwork.tlgrm.process_user.WebAppDataService;
import dpa.testwork.tlgrm.process_user.dto.TelegramUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Controller
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class WebAppController {

    private final WebAppDataService authService;

    @GetMapping
    public String handleWebApp(
            @RequestParam(name = "initData", required = false) String initData,
            @RequestParam(name = "redirected", required = false) String redirected,
            Model model) {

        if (initData == null) {
            return "true".equals(redirected) ? "auth-error" : "init-page";
        }

        if (!authService.validateInitData(initData)) {
            log.error("Invalid Telegram data signature");
            return "auth-error";
        }

        TelegramUser user = authService.parseUserData(initData);
        if (user == null) {
            log.error("Failed to parse user data");
            return "auth-error";
        }

        LocalDateTime lastAuth = Instant.ofEpochMilli(user.getLastAuth().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        log.info("Authenticated user: {} {}", user.getFirstName(), user.getLastName());

        model.addAttribute("userId", user.getId());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("photoUrl", user.getPhotoUrl());
        model.addAttribute("isPremium", user.getIsPremium());
        model.addAttribute("languageCode", user.getLanguageCode());
        model.addAttribute("lastAuth", lastAuth); // LocalDateTime

        return "user-profile";
    }
}