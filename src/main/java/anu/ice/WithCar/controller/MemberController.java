package anu.ice.WithCar.controller;

import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.domain.dto.UserDetailsEntity;
import anu.ice.WithCar.domain.entity.RecruitCarfull;
import anu.ice.WithCar.exception.NotLoginException;
import anu.ice.WithCar.service.CarfullRecruitService;
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
    private final CarfullRecruitService carfullRecruitService;

    @Autowired
    public MemberController(MemberService memberService, CarfullRecruitService carfullRecruitService) {
        this.memberService = memberService;
        this.carfullRecruitService = carfullRecruitService;
    }

    @GetMapping("/myInfo")
    public String showMyInfo(@AuthenticationPrincipal UserDetailsEntity member) {

        return new JSONObject(member.getMember()).toString();
    }

    @GetMapping("/myInfo/recruit")
    public List<RecruitCarfull> viewMyRecruitCarfullList(@AuthenticationPrincipal UserDetailsEntity member) {
        if(member == null) throw new NotLoginException();

        return carfullRecruitService.getMyCarfullRecruit(member.getMember());
    }

    @GetMapping("/myInfo/apply")
    public List<RecruitCarfull> viewMyAppliedCarfullList(@AuthenticationPrincipal UserDetailsEntity member,
                                                         @RequestParam(name = "showDeleted", required = false, defaultValue = "false") Boolean showDeleted) {
        if(member == null) throw new NotLoginException();

        return carfullRecruitService.getMyAppliedCarfullRecruit(member.getMember(), showDeleted);
    }

    @PostMapping("/myInfo/change-pw")
    public Member changePasswordController(@AuthenticationPrincipal UserDetailsEntity member,
                                           @RequestParam("pw") String pw){
        if(member == null) throw new NotLoginException();

        return memberService.changePassword(member.getMember(), pw);
    }

    @GetMapping("/admin/memberList")
    public List<Member> showMemberList() {
        return memberService.getMemberList();
    }

}
