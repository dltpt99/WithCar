package anu.ice.WithCar.service;

import anu.ice.WithCar.domain.dto.EditRecruitCarfullForm;
import anu.ice.WithCar.domain.dto.WriteRecruitCarfullForm;
import anu.ice.WithCar.domain.entity.ApplyRecruitCarfull;
import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.domain.entity.RecruitCarfull;
import anu.ice.WithCar.exception.*;
import anu.ice.WithCar.repository.ApplyCarfullRecruitRepository;
import anu.ice.WithCar.repository.CarfullRecruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        return carfullRecruitRepository.findAllByDeletedFalse();
    }

    public RecruitCarfull viewCarfullRecruit(long no) {
        RecruitCarfull recruitCarfull =  carfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitNotFoundException::new);
        if(recruitCarfull.isDeleted()) throw new CarfullRecruitDeletedException();

        increaseViewCarFull(recruitCarfull);
        return recruitCarfull;
    }

    public RecruitCarfull applyCarfullRecruit(long no, Member member) {
        RecruitCarfull recruitCarfull =  carfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitNotFoundException::new);

        //신청하려는 카풀이 삭제되었는지 검증
        if(recruitCarfull.isDeleted()) throw new CarfullRecruitDeletedException();

        //신청하려는 카풀을 이미 신청했는지 검증
        if(isCarfullRecruitApplied(recruitCarfull, member)) throw new CarfullRecruitAlreadyAppliedException();

        ApplyRecruitCarfull apply = new ApplyRecruitCarfull(recruitCarfull, member);

        carfullRecruitRepository.save(recruitCarfull);
        applyCarfullRecruitRepository.save(apply);
        return recruitCarfull;
    }

    //카풀 신청한 것 취소
    public RecruitCarfull cancelApplyCarfullRecruit(long no, Member member) {
        RecruitCarfull recruitCarfull = carfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitNotFoundException::new);

        //취소하려는 카풀이 삭제되었는지 검증
        if(recruitCarfull.isDeleted()) throw new CarfullRecruitDeletedException();

        //신청한 카풀이 맞는지 검증
        if(!isCarfullRecruitApplied(recruitCarfull, member)) throw new CarfullRecruitNotAppliedException();

        ApplyRecruitCarfull applyRecruitCarfull = getCarfullRecruitApplied(recruitCarfull, member);

        applyRecruitCarfull.setCancelled(true);
        recruitCarfull.applyCountDown();

        carfullRecruitRepository.save(recruitCarfull);
        applyCarfullRecruitRepository.save(applyRecruitCarfull);

        return recruitCarfull;
    }

    //모집글을 작성한 작성자가 카풀 신청자를 수락하는 기능
    public boolean acceptCarfullRecruitApply(Member member, long no) {
        ApplyRecruitCarfull applyRecruitCarfull = applyCarfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitApplyNotfoundException::new);
        RecruitCarfull recruitCarfull = applyRecruitCarfull.getRecruitCarfull();

        //작성자가 맞는지 검증
        if(!isCarfullRecruitOwner(recruitCarfull, member)) throw new NotCarfullRecruitWriterException();

        //취소한 신청인지 검증
        if(applyRecruitCarfull.isCancelled()) throw new CarfullRecruitApplyCancelledException();

        recruitCarfull.setApplyPersonCount(
                (short) (recruitCarfull.getApplyPersonCount() + 1)
        );

        applyRecruitCarfull.setAccepted(true);

        carfullRecruitRepository.save(recruitCarfull);
        applyCarfullRecruitRepository.save(applyRecruitCarfull);

        return true;
    }

    public boolean checkCarfullRecruitOwner(Member member, long no) {
        RecruitCarfull recruitCarfull = carfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitNotFoundException::new);
        return isCarfullRecruitOwner(recruitCarfull, member);
    }

    public boolean isCarfullRecruitOwner(RecruitCarfull recruitCarfull, Member member) {
        return Objects.equals(recruitCarfull.getWriteMember().getIdNumber(), member.getIdNumber());
    }

    public boolean checkCarfullRecruitApplied(Member member, long no) {
        RecruitCarfull recruitCarfull = carfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitNotFoundException::new);

        return isCarfullRecruitApplied(recruitCarfull, member);

    }

    public boolean isCarfullRecruitApplied(RecruitCarfull recruitCarfull, Member member) {
        applyCarfullRecruitRepository.findAllByRecruitCarfullAndApplicantAndCancelledFalse(recruitCarfull, member)
                .orElseThrow(CarfullRecruitNotFoundException::new);

        return true;
    }

    public ApplyRecruitCarfull getCarfullRecruitApplied(RecruitCarfull recruitCarfull, Member member) {
        return applyCarfullRecruitRepository.findAllByRecruitCarfullAndApplicantAndCancelledFalse(recruitCarfull, member)
                .orElseThrow(CarfullRecruitNotFoundException::new);
    }

    private void increaseViewCarFull(RecruitCarfull recruitCarfull) {
        recruitCarfull.setView(
                recruitCarfull.getView() + 1
        );
    }
}
