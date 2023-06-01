package anu.ice.WithCar.controller;

import anu.ice.WithCar.domain.entity.RecruitCarfull;
import anu.ice.WithCar.domain.dto.UserDetailsEntity;
import anu.ice.WithCar.domain.dto.EditRecruitCarfullForm;
import anu.ice.WithCar.domain.dto.WriteRecruitCarfullForm;
import anu.ice.WithCar.exception.NotLoginException;
import anu.ice.WithCar.service.CarfullRecruitService;
import anu.ice.WithCar.domain.entity.ApplyRecruitCarfull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarFullRecruitController {
    private final CarfullRecruitService carfullRecruitService;

    @Autowired
    public CarFullRecruitController(CarfullRecruitService carfullRecruitService) {
        this.carfullRecruitService = carfullRecruitService;
    }

    @GetMapping("/recruit")
    public List<RecruitCarfull> viewRecruitCarfullPage() {
        return carfullRecruitService.viewCarfullRecruitPage();
    }

    @PostMapping("/recruit/new")
    public RecruitCarfull writeRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                              WriteRecruitCarfullForm form) {
        if(member == null) throw new NotLoginException();

        return carfullRecruitService.writeCarfullRecruit(member.getMember(), form);
    }

    @GetMapping("/recruit/{no}")
    public RecruitCarfull viewRecruitCarfull(@PathVariable("no") long no) {
        return carfullRecruitService.viewCarfullRecruit(no);
    }

    @PostMapping("/recruit/edit")
    public RecruitCarfull editRecruitCarfull(EditRecruitCarfullForm form,
                                             @AuthenticationPrincipal UserDetailsEntity member) {
        if(member == null) throw new NotLoginException();

        return carfullRecruitService.editCarfullRecruit(form);
    }

    @PostMapping("/recruit/apply")
    public String applyRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                              @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return String.valueOf(carfullRecruitService.applyCarfullRecruit(no, member.getMember()));
    }

    @PostMapping("/recruit/cancel")
    public String applyCancelRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                                    @RequestParam("recruit_ID") long no) {
        return String.valueOf(carfullRecruitService.cancelApplyCarfullRecruit(no, member.getMember()));
    }

    // 카풀 모집글에 대해 거절되지 않은 리스트들을 보여줌
    // 카풀 운전자가 이 리스트들을 보고 수락/거절을 해야 됨
    @PostMapping("/recruit/applyList")
    public List<ApplyRecruitCarfull> getAppliesForCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                                          @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return carfullRecruitService.getAppliesForRecruitCarfullWithoutDenied(member.getMember(), no);
    }

    // 카풀 모집글에 수락된, 그러니까 함께 출발 예정인 리스트를 보여줌
    @PostMapping("/recruit/carfullMember")
    public List<ApplyRecruitCarfull> getAccpetedAppliesForCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                                                  @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return carfullRecruitService.getAcceptedAppliesForRecruitCarfull(member.getMember(), no);
    }

    // ApplyCarfullRecruit에 대한 ID를 받음
    @PostMapping("/recruit/accept")
    public String acceptRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                       @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return String.valueOf(carfullRecruitService.acceptCarfullRecruitApply(member.getMember(), no));
    }

    // ApplyCarfullRecruit에 대한 ID를 받음
    @PostMapping("/recruit/deny")
    public String denyRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                       @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return String.valueOf(carfullRecruitService.denyCarfullRecruitApply(member.getMember(), no));
    }

    // 카풀 모집글 ID와 킥당할 멤버 ID를 인자로 받음
    @PostMapping("/recruit/kick")
    public String kickMemberFromCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                        @RequestParam("recruit_ID") long recruit_ID,
                                        @RequestParam("member_ID") long member_id) {
        if (member == null) throw new NotLoginException();

        return String.valueOf(carfullRecruitService.kickMemberFromCarfull(member.getMember(), recruit_ID, member_id));
    }

    @PostMapping("/recruit/delete")
    public String deleteRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                       @RequestParam("recruit_ID") long no) {

        if(member == null) throw new NotLoginException();
        carfullRecruitService.deleteCarfullRecruit(no);

        return "0";
    }

    @PostMapping("/recruit/start")
    public String startRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                       @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();
        carfullRecruitService.startCarfullByWriter(member.getMember(), no);

        return "0";
    }

    @PostMapping("/recruit/start/agree")
    public String startAgreeRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                           @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();
        carfullRecruitService.startCarfullAgree(member.getMember(), no);

        return "0";
    }

    @PostMapping("/recruit/arrive")
    public String arriveRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                      @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();
        carfullRecruitService.arriveCarfullByWriter(member.getMember(), no);

        return "0";
    }

    @PostMapping("/recruit/arrive/agree")
    public String arriveAgreeRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                            @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();
        carfullRecruitService.arriveCarfullAgree(member.getMember(), no);

        return "0";
    }

    @PostMapping("/recruit/isowner")
    public String checkCarfullRecruitOwner(@AuthenticationPrincipal UserDetailsEntity member,
                                           @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return String.valueOf(
                carfullRecruitService.isCarfullRecruitOwner(member.getMember(), no)
        );
    }

    @PostMapping("/recruit/isApply")
    public String checkCarfullRecruitApplied(@AuthenticationPrincipal UserDetailsEntity member,
                                             @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return String.valueOf(
                carfullRecruitService.isCarfullRecruitApplied(member.getMember(), no)
        );
    }

    @PostMapping("/recruit/isAccept")
    public String checkCarfullRecruitAccepted(@AuthenticationPrincipal UserDetailsEntity member,
                                             @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return String.valueOf(
                carfullRecruitService.isCarfullRecruitAccepted(member.getMember(), no)
        );
    }

}
