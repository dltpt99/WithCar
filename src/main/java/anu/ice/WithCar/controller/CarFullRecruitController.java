package anu.ice.WithCar.controller;

import anu.ice.WithCar.entity.RecruitCarfull;
import anu.ice.WithCar.entity.UserDetailsEntity;
import anu.ice.WithCar.entity.EditRecruitCarfullForm;
import anu.ice.WithCar.entity.WriteRecruitCarfullForm;
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

    @PostMapping("/recruit/apply/{no}")
    public RecruitCarfull applyRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                              @PathVariable("no") long no) {
        return carfullRecruitService.applyCarfullRecruit(no, member.getMember());
    }

    @PostMapping("/recruit/apply/cancel/{no}")
    public RecruitCarfull applyCancelRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                                    @PathVariable("no") long no) {
        return carfullRecruitService.cancelApplyCarfullRecruit(no, member.getMember());
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
    @PostMapping("/recruit/delete/{no}")
    public String deleteRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                       @PathVariable("no") long no) {

        if(member == null) throw new NotLoginException();
        carfullRecruitService.deleteCarfullRecruit(no);

        return "0";
    }

    @GetMapping("/recruit/isowner")
    public String checkCarfullRecruitOwner(@AuthenticationPrincipal UserDetailsEntity member,
                                           @RequestParam("recruit_ID") long no) {
        return String.valueOf(
                carfullRecruitService.checkCarfullRecruitOwner(member.getMember(), no)
        );
    }

}
