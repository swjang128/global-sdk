package io.snplab.gsdk.access.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessController {
    /**
     * password 재설정 진입을 위한 view controller
     * @return
     */
    @GetMapping("/password-reset")
    public String password() {
        return "/index";
    }
}
