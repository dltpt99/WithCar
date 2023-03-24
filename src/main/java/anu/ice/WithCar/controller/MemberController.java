package anu.ice.WithCar.controller;

import anu.ice.WithCar.entity.Member;
import anu.ice.WithCar.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String showMyInfo() {

        return null;
    }

    @GetMapping("/admin/memberList")
    public List<Member> showMemberList() {
        return memberService.getMemberList();
    }

}
