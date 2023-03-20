package anu.ice.WithCar.controller;

import anu.ice.WithCar.entity.RecruitCarfull;
import anu.ice.WithCar.entity.UserDetailsEntity;
import anu.ice.WithCar.entity.writeRecruitCarfullForm;
import anu.ice.WithCar.service.CarfullRecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/recruit/new")
    public RecruitCarfull writeRecruitCarfull(@AuthenticationPrincipal UserDetailsEntity member,
                                              writeRecruitCarfullForm form) {
        return carfullRecruitService.writeCarfullRecruit(member.getMember(), form);
    }

    @GetMapping("/recruit/{no}")
    public RecruitCarfull viewRecruitCarfull(@PathVariable("no") long no) {
        return carfullRecruitService.viewCarfullRecruit(no);
    }
}
