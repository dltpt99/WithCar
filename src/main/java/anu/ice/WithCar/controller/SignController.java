package anu.ice.WithCar.controller;

import anu.ice.WithCar.entity.SignupForm;
import anu.ice.WithCar.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
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
        form.setPw(
                new BCryptPasswordEncoder().encode(form.getPw())
        );

        signService.signup_service(form);
        return "success";
    }
}
