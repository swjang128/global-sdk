package io.snplab.gsdk.access.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessController {
    /**
     * view controller for password reset email
     * @return
     */
    @GetMapping("/password/**")
    public String password() {
        return "/index";
    }
}
