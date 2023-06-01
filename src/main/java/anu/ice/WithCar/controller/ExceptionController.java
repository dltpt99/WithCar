package anu.ice.WithCar.controller;

import anu.ice.WithCar.exception.*;
import anu.ice.WithCar.exception.CarfullRecruit.*;
import anu.ice.WithCar.exception.Chat.ChatMessageNotFoundException;
import anu.ice.WithCar.exception.Chat.ChatRoomNotFoundExcpetion;
import anu.ice.WithCar.exception.Member.MemberNotFoundException;
import anu.ice.WithCar.exception.Member.NickDuplicateException;
import anu.ice.WithCar.exception.Member.SignFormIncludeNullException;
import anu.ice.WithCar.exception.Member.UserIdDuplicateException;
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

    // 회원가입 시 어떤 항목이라도 NULL 일때
    @ExceptionHandler(value = SignFormIncludeNullException.class)
    public String signFormIncludeNull() { return "signFormIncludeNull"; }

    //멤버 검색결과 찾지 못함
    @ExceptionHandler(value = MemberNotFoundException.class)
    public String memberNotFoundException() { return "memberNotFound"; }

    // 로그인이 되어있지 않음
    @ExceptionHandler(value = NotLoginException.class)
    public String notLoginError() {
        return "notLoginError";
    }

    // 카풀 게시글 열람 시도했으나 찾는 게시물이 없음
    @ExceptionHandler(value = CarfullRecruitNotFoundException.class)
    public String carfullRecruitNotfoundError() {
        return "carfullRecruitNotfoundError";
    }

    // 로그인 시 ID, PW 다름
    @ExceptionHandler(value = BadCredentialsException.class)
    public String badCredentialsException(BadCredentialsException e) {
        return e.getMessage();
    }

    //신청하려는 카풀의 제한을 초과함
    @ExceptionHandler(value = CarfullRecruitApplyLimitedOverException.class)
    public String carfullRecruitApplyLimitedOverException() { return "carfullRecruitLimitedOver"; }

    //특정 카풀 신청 내역을 DB에서 찾지 못함
    @ExceptionHandler(value = CarfullRecruitApplyNotfoundException.class)
    public String carfullRecruitApplyNotfoundException() { return "carfullRecruitApplyNotfound"; }

    //카풀 신청을 하지 않았음
    @ExceptionHandler(value = CarfullRecruitNotAppliedException.class)
    public String carfullRecruitNotAppliedException() { return "carfullRecruitNotApplied"; }

    //이미 해당 카풀을 신청했음
    @ExceptionHandler(value = CarfullRecruitAlreadyAppliedException.class)
    public String carfullRecruitAlreadyAppliedException() { return "carfullRecruitAlreadyApplied"; }

    //카풀 모집글 작성자가 아님
    @ExceptionHandler(value = NotCarfullRecruitWriterException.class)
    public String notCarfullRecruitWriterException() { return "notCarfullRecruitWriter"; }

    //카풀 신청이 취소되었음
    @ExceptionHandler(value = CarfullRecruitApplyCancelledException.class)
    public String carfullRecruitApplyCancelledException() { return "carfullRecruitApplyCancelled"; }

    //채팅방을 찾을 수 없음(새로 채팅방 생성)
    @ExceptionHandler(value = ChatRoomNotFoundExcpetion.class)
    public String chatRoomNotFoundException() { return "chatRoomNotFound"; }

    //채팅 메시지를 찾을 수 없음
    @ExceptionHandler(value = ChatMessageNotFoundException.class)
    public String chatMessageNotFoundException() { return "chatMessageNotFound"; }

    // 카풀 탑승자가 출발 동의할 때, 아직 운전자가 출발 상태로 변환하지 않았을 때 발생
    @ExceptionHandler(value = CarfullRecruitNotNeedStartAgreeException.class)
    public String carfullRecruitNotNeedStartAgree() { return "carfullRecruitNotNeedStartAgree"; }

    // 카풀 탑승자가 도착 동의할 때, 아직 운전자가 도착 상태로 변환하지 않았을 때 발생
    @ExceptionHandler(value = CarfullRecruitNotNeedArriveAgreeException.class)
    public String carfullRecruitNotNeedArriveAgree() { return "carfullRecruitNotNeedArriveAgree"; }
}
