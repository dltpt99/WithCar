package anu.ice.WithCar.controller;

import anu.ice.WithCar.domain.entity.RecruitCarfull;
import anu.ice.WithCar.domain.dto.UserDetailsEntity;
import anu.ice.WithCar.domain.dto.EditRecruitCarfullForm;
import anu.ice.WithCar.domain.dto.WriteRecruitCarfullForm;
import anu.ice.WithCar.exception.NotLoginException;
import anu.ice.WithCar.service.CarfullRecruitService;
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
        return String.valueOf(carfullRecruitService.applyCarfullRecruit(no, member.getMember()));
    }

    @PostMapping("/recruit/cancel")
    public String applyCancelRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                                    @RequestParam("recruit_ID") long no) {
        return String.valueOf(carfullRecruitService.cancelApplyCarfullRecruit(no, member.getMember()));
    }

    @PostMapping("/recruit/delete")
    public String deleteRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                       @RequestParam("recruit_ID") long no) {

        if(member == null) throw new NotLoginException();
        carfullRecruitService.deleteCarfullRecruit(no);

        return "0";
    }

    // ApplyCarfullRecruit에 대한 ID를 받음
    @PostMapping("/recruit/accept")
    public String acceptRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                       @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return String.valueOf(carfullRecruitService.acceptCarfullRecruitApply(member.getMember(), no));
    }

    @PostMapping("/recruit/isowner")
    public String checkCarfullRecruitOwner(@AuthenticationPrincipal UserDetailsEntity member,
                                           @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return String.valueOf(
                carfullRecruitService.checkCarfullRecruitOwner(member.getMember(), no)
        );
    }

    @PostMapping("/recruit/isApply")
    public String checkCarfullRecruitApplied(@AuthenticationPrincipal UserDetailsEntity member,
                                             @RequestParam("recruit_ID") long no) {
        if(member == null) throw new NotLoginException();

        return String.valueOf(
                carfullRecruitService.checkCarfullRecruitApplied(member.getMember(), no)
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
