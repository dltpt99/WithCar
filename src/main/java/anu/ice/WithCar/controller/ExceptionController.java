package anu.ice.WithCar.controller;

import anu.ice.WithCar.exception.*;
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

    // 로그인이 되어있지 않음
    @ExceptionHandler(value = NotLoginException.class)
    public String notLoginError() {
        return "notLoginError";
    }

    // 로그인 시 ID, PW 다름
    @ExceptionHandler(value = BadCredentialsException.class)
    public String badCredentialsException(BadCredentialsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = CarfullRecruitApplyLimitedOverException.class)
    public String carfullRecruitApplyLimitedOverException() { return "carfullRecruitLimitedOver"; }

    @ExceptionHandler(value = CarfullRecruitApplyNotfoundException.class)
    public String carfullRecruitApplyNotfoundException() { return "carfullRecruitApplyNotfound"; }

    @ExceptionHandler(value = MemberNotFoundException.class)
    public String memberNotFoundException() { return "memberNotFound"; }
}
