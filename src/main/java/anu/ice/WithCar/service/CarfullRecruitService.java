package anu.ice.WithCar.service;

import anu.ice.WithCar.entity.EditRecruitCarfullForm;
import anu.ice.WithCar.entity.Member;
import anu.ice.WithCar.entity.RecruitCarfull;
import anu.ice.WithCar.entity.WriteRecruitCarfullForm;
import anu.ice.WithCar.exception.CarfullRecruitDeletedException;
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
    }

    public List<RecruitCarfull> viewCarfullRecruitPage() {
        // 추후 페이징 기능 추가
        return carfullRecruitRepository.findAll();
    }

    public RecruitCarfull viewCarfullRecruit(long no) {
        RecruitCarfull recruitCarfull =  carfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitNotFoundException::new);
        if(recruitCarfull.isDeleted()) throw new CarfullRecruitDeletedException();

        increaseViewCarFull(recruitCarfull);
        return recruitCarfull;
    }

    public void increaseViewCarFull(RecruitCarfull recruitCarfull) {
        recruitCarfull.setView(
                recruitCarfull.getView() + 1
        );
    }
}
