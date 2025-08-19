package org.example.emailsend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuthController {

    @GetMapping("/")
    public String home() {
        return "index"; // Optional: return a home page
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OAuth2User user, Model model) {
        model.addAttribute("name", user.getAttribute("name"));
        model.addAttribute("email", user.getAttribute("email"));
        return "dashboard"; // return dashboard.html from templates
    }
}
