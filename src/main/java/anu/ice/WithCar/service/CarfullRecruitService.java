package anu.ice.WithCar.service;

import anu.ice.WithCar.entity.Member;
import anu.ice.WithCar.entity.RecruitCarfull;
import anu.ice.WithCar.entity.writeRecruitCarfullForm;
import anu.ice.WithCar.exception.CarfullRecruitNotFoundException;
import anu.ice.WithCar.repository.CarfullRecruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarfullRecruitService {
    private final CarfullRecruitRepository carfullRecruitRepository;

    @Autowired
    public CarfullRecruitService(CarfullRecruitRepository carfullRecruitRepository) {
        this.carfullRecruitRepository = carfullRecruitRepository;
    }

    public RecruitCarfull writeCarfullRecruit(Member member, writeRecruitCarfullForm form) {
        RecruitCarfull recruitCarfull = new RecruitCarfull(member, form);
        return carfullRecruitRepository.save(recruitCarfull);
    }

    public List<RecruitCarfull> viewCarfullRecruitPage() {
        // 추후 페이징 기능 추가
        return carfullRecruitRepository.findAll();
    }

    public RecruitCarfull viewCarfullRecruit(long boardID) {
        RecruitCarfull recruitCarfull =  carfullRecruitRepository.findById(boardID).orElseThrow(CarfullRecruitNotFoundException::new);
        increaseViewCarFull(recruitCarfull);
        return recruitCarfull;
    }

    public void increaseViewCarFull(RecruitCarfull recruitCarfull) {
        recruitCarfull.setView(
                recruitCarfull.getView() + 1
        );
    }
}
