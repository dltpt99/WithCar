package anu.ice.WithCar.controller;

import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.domain.dto.UserDetailsEntity;
import anu.ice.WithCar.service.MemberService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/myInfo/change-pw")
    public Member changePasswordController(@AuthenticationPrincipal UserDetailsEntity member,
                                           @RequestParam("pw") String pw){
        return memberService.changePassword(member.getMember(), pw);
    }

    @GetMapping("/admin/memberList")
    public List<Member> showMemberList() {
        return memberService.getMemberList();
    }

}
