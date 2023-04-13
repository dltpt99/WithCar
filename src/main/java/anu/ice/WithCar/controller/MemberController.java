package anu.ice.WithCar.controller;

import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.domain.dto.UserDetailsEntity;
import anu.ice.WithCar.service.MemberService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/myInfo")
    public String showMyInfo(@AuthenticationPrincipal UserDetailsEntity member) {

        return new JSONObject(member.getMember()).toString();
    }

    @GetMapping("/admin/memberList")
    public List<Member> showMemberList() {
        return memberService.getMemberList();
    }

}
