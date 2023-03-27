package anu.ice.WithCar.controller;

import anu.ice.WithCar.exception.CarfullRecruitNotFoundException;
import anu.ice.WithCar.exception.NickDuplicateException;
import anu.ice.WithCar.exception.NotLoginException;
import anu.ice.WithCar.exception.UserIdDuplicateException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    // 회원가입 시 닉네임 중복
    @ExceptionHandler(value = NickDuplicateException.class)
    public String nickDuplicateError() {
        return "nickDuplicateError";
    }

    // 회원가입 시 ID 중복
    @ExceptionHandler(value = UserIdDuplicateException.class)
    public String userIdDuplicateError() {
        return "userIdDuplicateError";
    }

    // 카풀 게시글 열람 시도했으나 찾는 게시물이 없음
    @ExceptionHandler(value = CarfullRecruitNotFoundException.class)
    public String carfullRecruitNotfoundError() {
        return "carfullRecruitNotfoundError";
    }

    @ExceptionHandler(value = NotLoginException.class)
    public String notLoginError() {
        return "notLoginError";
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public String badCredentialsException(BadCredentialsException e) {
        return e.getMessage();
    }
}
