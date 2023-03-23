package anu.ice.WithCar.controller;

import anu.ice.WithCar.exception.CarfullRecruitNotFoundException;
import anu.ice.WithCar.exception.NickDuplicateException;
import anu.ice.WithCar.exception.NotLoginException;
import anu.ice.WithCar.exception.UserIdDuplicateException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = NickDuplicateException.class)
    public String nickDuplicateError() {
        return "nickDuplicateError";
    }

    @ExceptionHandler(value = UserIdDuplicateException.class)
    public String userIdDuplicateError() {
        return "userIdDuplicateError";
    }

    @ExceptionHandler(value = CarfullRecruitNotFoundException.class)
    public String carfullRecruitNotfoundError() {
        return "carfullRecruitNotfoundError";
    }

    @ExceptionHandler(value = NotLoginException.class)
    public String notLoginError() {
        return "notLoginError";
    }
}
