package anu.ice.WithCar.service;

import anu.ice.WithCar.entity.*;
import anu.ice.WithCar.exception.CarfullRecruitDeletedException;
import anu.ice.WithCar.exception.CarfullRecruitNotFoundException;
import anu.ice.WithCar.repository.ApplyCarfullRecruitRepository;
import anu.ice.WithCar.repository.CarfullRecruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarfullRecruitService {
    private final CarfullRecruitRepository carfullRecruitRepository;
    private final ApplyCarfullRecruitRepository applyCarfullRecruitRepository;


    @Autowired
    public CarfullRecruitService(CarfullRecruitRepository carfullRecruitRepository, ApplyCarfullRecruitRepository applyCarfullRecruitRepository) {
        this.carfullRecruitRepository = carfullRecruitRepository;
        this.applyCarfullRecruitRepository = applyCarfullRecruitRepository;
    }

    public RecruitCarfull writeCarfullRecruit(Member member, WriteRecruitCarfullForm form) {
        RecruitCarfull recruitCarfull = new RecruitCarfull(member, form);
        return carfullRecruitRepository.save(recruitCarfull);
    }

    public RecruitCarfull editCarfullRecruit(EditRecruitCarfullForm form) {
        RecruitCarfull recruitCarfull = carfullRecruitRepository
                .findById(form.getRecruitCarfullID())
                .orElseThrow(CarfullRecruitNotFoundException::new);

        recruitCarfull.setBoardTitle(form.getBoardTitle());
        recruitCarfull.setFee(form.getFee());
        recruitCarfull.setComment(form.getComment());
        recruitCarfull.setStartPoint(form.getStartPoint());
        recruitCarfull.setEndPoint(form.getEndPoint());
        return carfullRecruitRepository.save(recruitCarfull);
    }

    public void deleteCarfullRecruit(long no) {
        RecruitCarfull recruitCarfull = carfullRecruitRepository
                .findById(no)
                .orElseThrow(CarfullRecruitNotFoundException::new);
        if(recruitCarfull.isDeleted()) throw new CarfullRecruitDeletedException();
        recruitCarfull.setDeleted(true);
        carfullRecruitRepository.save(recruitCarfull);
    }

    public List<RecruitCarfull> viewCarfullRecruitPage() {
        // 추후 페이징 기능 추가
        return carfullRecruitRepository.findAllByDeletedIsFalse();
    }

    public RecruitCarfull viewCarfullRecruit(long no) {
        RecruitCarfull recruitCarfull =  carfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitNotFoundException::new);
        if(recruitCarfull.isDeleted()) throw new CarfullRecruitDeletedException();

        increaseViewCarFull(recruitCarfull);
        return recruitCarfull;
    }

    public RecruitCarfull applyCarfullRecruit(long no, Member member) {
        RecruitCarfull recruitCarfull =  carfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitNotFoundException::new);
        if(recruitCarfull.isDeleted()) throw new CarfullRecruitDeletedException();

        ApplyRecruitCarfull apply = new ApplyRecruitCarfull(recruitCarfull, member);
        applyCarfullRecruitRepository.save(apply);
        return recruitCarfull;
    }
    private void increaseViewCarFull(RecruitCarfull recruitCarfull) {
        recruitCarfull.setView(
                recruitCarfull.getView() + 1
        );
    }
}
