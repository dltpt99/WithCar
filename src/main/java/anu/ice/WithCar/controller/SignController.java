package anu.ice.WithCar.controller;

import anu.ice.WithCar.entity.*;
import anu.ice.WithCar.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignController {
    private final SignService signService;

    @Autowired
    public SignController(SignService signService) {
        this.signService = signService;
    }

    @PostMapping("/signup")
    public String signupController(SignupForm form) {
        signService.signup_service(form);
        return "success";
    }

    @GetMapping("/signup/check/nick")
    public String checkNickDuplicateOnSigup(@RequestParam("nick") String nick) {
        signService.check_duplicate_nick(nick);
        return "0";
    }

    @GetMapping("/signup/check/id")
    public String checkUserIdDuplicateOnSigup(@RequestParam("id") String id) {
        signService.check_duplicate_user_id(id);
        return "0";
    }

    @PostMapping("/login-process")
    public LoginResponse loginProcess(LoginRequest request) {
        return signService.login(request);
    }

}
