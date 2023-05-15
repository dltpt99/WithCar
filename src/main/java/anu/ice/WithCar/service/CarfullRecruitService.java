package anu.ice.WithCar.service;

import anu.ice.WithCar.domain.dto.EditRecruitCarfullForm;
import anu.ice.WithCar.domain.dto.WriteRecruitCarfullForm;
import anu.ice.WithCar.domain.entity.ApplyRecruitCarfull;
import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.domain.entity.RecruitCarfull;
import anu.ice.WithCar.exception.CarfullRecruit.*;
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
    private final ChatForRecruitService chatForRecruitService;

    @Autowired
    public CarfullRecruitService(CarfullRecruitRepository carfullRecruitRepository, ApplyCarfullRecruitRepository applyCarfullRecruitRepository, ChatForRecruitService chatForRecruitService) {
        this.carfullRecruitRepository = carfullRecruitRepository;
        this.applyCarfullRecruitRepository = applyCarfullRecruitRepository;
        this.chatForRecruitService = chatForRecruitService;
    }

    public RecruitCarfull writeCarfullRecruit(Member member, WriteRecruitCarfullForm form) {
        RecruitCarfull recruitCarfull = new RecruitCarfull(member, form);
        // ChatRoom 개설
        chatForRecruitService.createNewChatRoomForRecruit(recruitCarfull);

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
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);
        recruitCarfull.setDeleted(true);
        carfullRecruitRepository.save(recruitCarfull);
    }

    public List<RecruitCarfull> viewCarfullRecruitPage() {
        // 추후 페이징 기능 추가
        return carfullRecruitRepository.findAllByDeletedFalse();
    }

    public RecruitCarfull viewCarfullRecruit(long no) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        increaseViewCarFull(recruitCarfull);
        return recruitCarfull;
    }

    public List<RecruitCarfull> getMyCarfullRecruit(Member member) {
        return carfullRecruitRepository.findAllByDeletedFalseAndWriteMember(member);
    }

    public List<RecruitCarfull> getMyAppliedCarfullRecruit(Member member, boolean showDeleted) {
        return showDeleted ? applyCarfullRecruitRepository.findAllByApplicant(member).stream().map(ApplyRecruitCarfull::getRecruitCarfull).toList() :
                applyCarfullRecruitRepository.findAllByApplicantAndCancelledFalse(member).stream().map(ApplyRecruitCarfull::getRecruitCarfull).toList();
    }

    public boolean applyCarfullRecruit(long no, Member member) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        //신청하려는 카풀을 이미 신청했는지 검증
        if(isCarfullRecruitApplied(recruitCarfull, member)) throw new CarfullRecruitAlreadyAppliedException();

        ApplyRecruitCarfull apply = new ApplyRecruitCarfull(recruitCarfull, member);

        recruitCarfull.applyCountUp();
        //채팅방에 멤버 추가
        chatForRecruitService.addNewMemberToChatRoom(recruitCarfull, member);

        carfullRecruitRepository.save(recruitCarfull);
        applyCarfullRecruitRepository.save(apply);

        return true;
    }

    //카풀 신청한 것 취소
    public boolean cancelApplyCarfullRecruit(long no, Member member) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        //신청한 카풀이 맞는지 검증
        if(!isCarfullRecruitApplied(recruitCarfull, member)) throw new CarfullRecruitNotAppliedException();

        ApplyRecruitCarfull applyRecruitCarfull = getCarfullRecruitApply(recruitCarfull, member);

        applyRecruitCarfull.setCancelled(true);
        recruitCarfull.applyCountDown();
        //채팅방에서 멤버 제거
        chatForRecruitService.removeMemberFromChatRoom(recruitCarfull, member);

        carfullRecruitRepository.save(recruitCarfull);
        applyCarfullRecruitRepository.save(applyRecruitCarfull);

        return true;
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
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);
        return isCarfullRecruitOwner(recruitCarfull, member);
    }

    public boolean isCarfullRecruitOwner(RecruitCarfull recruitCarfull, Member member) {
        return Objects.equals(recruitCarfull.getWriteMember().getIdNumber(), member.getIdNumber());
    }

    public boolean checkCarfullRecruitApplied(Member member, long no) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        return isCarfullRecruitApplied(recruitCarfull, member);

    }

    public boolean isCarfullRecruitApplied(RecruitCarfull recruitCarfull, Member member) {
        return applyCarfullRecruitRepository.findAllByRecruitCarfullAndApplicantAndCancelledFalse(recruitCarfull, member)
                .isPresent();
    }

    public ApplyRecruitCarfull getCarfullRecruitApply(RecruitCarfull recruitCarfull, Member member) {
        return applyCarfullRecruitRepository.findAllByRecruitCarfullAndApplicantAndCancelledFalse(recruitCarfull, member)
                .orElseThrow(CarfullRecruitNotFoundException::new);
    }

    public boolean isCarfullRecruitAccepted(Member member, long no) {
        return applyCarfullRecruitRepository.findAllByRecruitCarfullAndApplicantAndCancelledFalse(
                getRecruitCarfull(no), member)
                .orElseThrow(CarfullRecruitApplyNotfoundException::new).isAccepted();
    }

    private RecruitCarfull getRecruitCarfull(long no) {
        RecruitCarfull recruitCarfull =  carfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitNotFoundException::new);
        if(recruitCarfull.isDeleted()) throw new CarfullRecruitDeletedException();
        return recruitCarfull;
    }

    private void increaseViewCarFull(RecruitCarfull recruitCarfull) {
        recruitCarfull.setView(
                recruitCarfull.getView() + 1
        );
    }
}
