package anu.ice.WithCar.service;

import anu.ice.WithCar.domain.dto.EditRecruitCarfullForm;
import anu.ice.WithCar.domain.dto.WriteRecruitCarfullForm;
import anu.ice.WithCar.domain.entity.*;
import anu.ice.WithCar.exception.CarfullRecruit.*;
import anu.ice.WithCar.repository.ApplyCarfullRecruitRepository;
import anu.ice.WithCar.repository.CarfullRecruitRepository;
import anu.ice.WithCar.repository.RecruitCarfullArriveAgreementRepository;
import anu.ice.WithCar.repository.RecruitCarfullStartAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CarfullRecruitService {
    private final CarfullRecruitRepository carfullRecruitRepository;
    private final ApplyCarfullRecruitRepository applyCarfullRecruitRepository;
    private final ChatForRecruitService chatForRecruitService;
    private final RecruitCarfullArriveAgreementRepository arriveAgreementRepository;
    private final RecruitCarfullStartAgreementRepository startAgreementRepository;
    private final MemberService memberService;

    @Autowired
    public CarfullRecruitService(CarfullRecruitRepository carfullRecruitRepository, ApplyCarfullRecruitRepository applyCarfullRecruitRepository, ChatForRecruitService chatForRecruitService, RecruitCarfullArriveAgreementRepository arriveAgreementRepository, RecruitCarfullStartAgreementRepository startAgreementRepository, MemberService memberService) {
        this.carfullRecruitRepository = carfullRecruitRepository;
        this.applyCarfullRecruitRepository = applyCarfullRecruitRepository;
        this.chatForRecruitService = chatForRecruitService;
        this.arriveAgreementRepository = arriveAgreementRepository;
        this.startAgreementRepository = startAgreementRepository;
        this.memberService = memberService;
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
        recruitCarfull.increaseView();

        return carfullRecruitRepository.save(recruitCarfull);
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
        if (isCarfullRecruitApplied(recruitCarfull, member)) throw new CarfullRecruitAlreadyAppliedException();

        ApplyRecruitCarfull apply = new ApplyRecruitCarfull(recruitCarfull, member);
        applyCarfullRecruitRepository.save(apply);

        return true;
    }

    //카풀 신청한 것 취소
    public boolean cancelApplyCarfullRecruit(long no, Member member) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        //신청한 카풀이 맞는지 검증
        if (!isCarfullRecruitApplied(recruitCarfull, member)) throw new CarfullRecruitNotAppliedException();

        ApplyRecruitCarfull applyRecruitCarfull = getCarfullRecruitApply(recruitCarfull, member);
        applyRecruitCarfull.setCancelled(true);

        applyCarfullRecruitRepository.save(applyRecruitCarfull);
        return true;
    }

    //모집글을 작성한 작성자가 카풀 신청자를 수락하는 기능
    // ApplyRecruitCarfull에 대한 ID를 인자로 받음
    public boolean acceptCarfullRecruitApply(Member member, long no) {
        ApplyRecruitCarfull applyRecruitCarfull = applyCarfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitApplyNotfoundException::new);
        RecruitCarfull recruitCarfull = applyRecruitCarfull.getRecruitCarfull();

        //작성자가 맞는지 검증
        if (!isCarfullRecruitOwner(recruitCarfull, member)) throw new NotCarfullRecruitWriterException();

        //취소한 신청인지 검증
        if (applyRecruitCarfull.isCancelled()) throw new CarfullRecruitApplyCancelledException();

        // 이미 승낙된 신청인지 검증
        if (applyRecruitCarfull.isAccepted()) throw new CarfullRecruitAlreadyAccpetedException();

        // 이미 거절된 신청인지 검증
        if (applyRecruitCarfull.isDenied()) throw new CarfullRecruitAlreadyDenidedException();

        // 수락 상태로 변경 및 applyCount 증가 (탑승자로 인식)
        applyRecruitCarfull.setAccepted(true);
        applyCarfullRecruitRepository.save(applyRecruitCarfull);

        // 카풀 신청인원 1 증가
        recruitCarfull.applyCountUp();
        carfullRecruitRepository.save(recruitCarfull);

        //채팅방에 멤버 추가
        chatForRecruitService.addNewMemberToChatRoom(recruitCarfull, member);

        return true;
    }

    //모집글을 작성한 작성자가 카풀 신청자를 거절하는 기능
    // ApplyRecruitCarfull에 대한 ID를 인자로 받음
    public boolean denyCarfullRecruitApply(Member member, long no) {
        ApplyRecruitCarfull applyRecruitCarfull = applyCarfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitApplyNotfoundException::new);
        RecruitCarfull recruitCarfull = applyRecruitCarfull.getRecruitCarfull();

        //작성자가 맞는지 검증
        if (!isCarfullRecruitOwner(recruitCarfull, member)) throw new NotCarfullRecruitWriterException();

        //취소한 신청인지 검증
        if (applyRecruitCarfull.isCancelled()) throw new CarfullRecruitApplyCancelledException();

        // 이미 승낙된 신청인지 검증
        if (applyRecruitCarfull.isAccepted()) throw new CarfullRecruitAlreadyAccpetedException();

        // 이미 거절된 신청인지 검증
        if (applyRecruitCarfull.isDenied()) throw new CarfullRecruitAlreadyDenidedException();

        // 거절 상태로 변경
        applyRecruitCarfull.setDenied(true);
        applyCarfullRecruitRepository.save(applyRecruitCarfull);

        return true;
    }

    // 카풀 탑승자를 추방하는 기능
    public boolean kickMemberFromCarfull(Member owner, long recruit_id, long member_id) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(recruit_id);

        //작성자가 맞는지 검증
        if (!isCarfullRecruitOwner(recruitCarfull, owner)) throw new NotCarfullRecruitWriterException();

        Member member = memberService.getMember(member_id);

        //신청자가 맞는지 검증
        ApplyRecruitCarfull applyRecruitCarfull = applyCarfullRecruitRepository.
                findByRecruitCarfullAndApplicantAndAcceptedTrue(recruitCarfull, member)
                .orElseThrow(CarfullRecruitApplyNotfoundException::new);

        //수락한걸 거절로 바꿈
        applyRecruitCarfull.setAccepted(false);
        applyRecruitCarfull.setDenied(true);
        applyCarfullRecruitRepository.save(applyRecruitCarfull);

        // 인원 1명 감소 시킴
        recruitCarfull.applyCountDown();
        carfullRecruitRepository.save(recruitCarfull);

        //채팅방에서 멤버 제거
        chatForRecruitService.removeMemberFromChatRoom(recruitCarfull, member);

        return true;
    }

    // 작성자가 카풀을 출발하고자 함
    // 카풀 탑승자들에게 출발 동의를 받아야 함
    public void startCarfullByWriter(Member member, long no) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        //작성자가 맞는지 검증
        if (!isCarfullRecruitOwner(recruitCarfull, member)) throw new NotCarfullRecruitWriterException();

        // 동의가 필요한 상태로 변환
        recruitCarfull.setNeedStartAgree(true);
        carfullRecruitRepository.save(recruitCarfull);
    }

    //카풀 탑승자가 카풀 출발에 동의
    public void startCarfullAgree(Member member, long no) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        //신청한 카풀이 맞는지 검증
        if (!isCarfullRecruitApplied(recruitCarfull, member)) throw new CarfullRecruitNotAppliedException();

        ApplyRecruitCarfull applyRecruitCarfull = applyCarfullRecruitRepository.
                findByRecruitCarfullAndApplicantAndCancelledFalse(recruitCarfull, member).
                orElseThrow(CarfullRecruitApplyNotfoundException::new);

        RecruitCarfullStartAgreement startAgreement = new RecruitCarfullStartAgreement(applyRecruitCarfull);
        startAgreementRepository.save(startAgreement);

        //각각의 동의마다 모든 신청자들이 카풀 출발에 동의했는지 검증
        if (isStartAllAgreed(recruitCarfull)) setCarfullRecruitStart(recruitCarfull);
    }

    // 카풀 모집글을 출발 상태로 전환
    private void setCarfullRecruitStart(RecruitCarfull recruitCarfull) {
        recruitCarfull.setStarted(true);
        carfullRecruitRepository.save(recruitCarfull);
    }

    // 카풀의 모든 탑승자들이 동의했는지 확인
    private boolean isStartAllAgreed(RecruitCarfull recruitCarfull) {
        List<ApplyRecruitCarfull> applies = applyCarfullRecruitRepository.findAllByRecruitCarfullAndAcceptedTrue(recruitCarfull);

        // 신청서들과 동의서들을 비교해서 모두 일치하면 출발
        // 모두 일치하지 않아도 누가 동의하지 않았는지는 알 수 있도록 별개의 함수 짜야함
        for (ApplyRecruitCarfull apply : applies) {
            // 신청서를 가지고 동의서를 찾고, 동의서가 없으면 false 리턴 ( 모두 동의하지 않았다 )
            if (startAgreementRepository.findByApplyRecruitCarfull(apply).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // 작성자가 카풀을 도착상태로 신청함
    // 카풀 탑승자들에게 도착 동의를 받아야함
    public void arriveCarfullByWriter(Member member, long no) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        //작성자가 맞는지 검증
        if (!isCarfullRecruitOwner(recruitCarfull, member)) throw new NotCarfullRecruitWriterException();

        // 동의가 필요한 상태로 변환
        recruitCarfull.setNeedArriveAgree(true);
        carfullRecruitRepository.save(recruitCarfull);
    }

    //카풀 탑승자가 카풀 도착에 동의
    public void arriveCarfullAgree(Member member, long no) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        //신청한 카풀이 맞는지 검증
        if (!isCarfullRecruitApplied(recruitCarfull, member)) throw new CarfullRecruitNotAppliedException();

        ApplyRecruitCarfull applyRecruitCarfull = applyCarfullRecruitRepository.
                findByRecruitCarfullAndApplicantAndCancelledFalse(recruitCarfull, member).
                orElseThrow(CarfullRecruitApplyNotfoundException::new);

        RecruitCarfullArriveAgreement arriveAgreement = new RecruitCarfullArriveAgreement(applyRecruitCarfull);
        arriveAgreementRepository.save(arriveAgreement);

        //각각의 동의마다 모든 신청자들이 카풀 도착에 동의했는지 검증
        if (isArriveAllAgreed(recruitCarfull)) setCarfullRecruitArrive(recruitCarfull);

    }

    // 카풀을 도착 상태로 변경
    private void setCarfullRecruitArrive(RecruitCarfull recruitCarfull) {
        recruitCarfull.setArrived(true);
        carfullRecruitRepository.save(recruitCarfull);
    }

    private boolean isArriveAllAgreed(RecruitCarfull recruitCarfull) {
        // 카풀의 모든 탑승자들이 동의했는지 확인
        List<ApplyRecruitCarfull> applies = applyCarfullRecruitRepository.findAllByRecruitCarfullAndAcceptedTrue(recruitCarfull);

        // 신청서들과 동의서들을 비교해서 모두 일치하면 도착
        // 모두 일치하지 않아도 누가 동의하지 않았는지는 알 수 있도록 별개의 함수 짜야함
        for (ApplyRecruitCarfull apply : applies) {
            // 신청서를 가지고 동의서를 찾고, 동의서가 없으면 false 리턴 ( 모두 동의하지 않았다 )
            if (arriveAgreementRepository.findByApplyRecruitCarfull(apply).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // 카풀 모집글에 대해 신청서들을 보여줌
    // 운전자가 수락/거절 할때 보여주는 리스트
    public List<ApplyRecruitCarfull> getAppliesForRecruitCarfullWithoutDenied(Member member, long no) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        //작성자가 맞는지 검증
        if (!isCarfullRecruitOwner(recruitCarfull, member)) throw new NotCarfullRecruitWriterException();

        return applyCarfullRecruitRepository.findAllByRecruitCarfullAndDeniedFalseAndCancelledFalse(recruitCarfull);
    }

    // 카풀 모집글에 수락된, 그러니까 함께 출발 예정인 리스트를 보여줌
    public List<ApplyRecruitCarfull> getAcceptedAppliesForRecruitCarfull(Member member, long no) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);

        //작성자가 맞는지 검증
        if (!isCarfullRecruitOwner(recruitCarfull, member)) throw new NotCarfullRecruitWriterException();

        return applyCarfullRecruitRepository.findAllByRecruitCarfullAndAcceptedTrue(recruitCarfull);
    }

    // 카풀 작성자가 맞는지 검증
    public boolean isCarfullRecruitOwner(Member member, long no) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);
        return isCarfullRecruitOwner(recruitCarfull, member);
    }

    // 카풀 작성자가 맞는지 검증 2
    private boolean isCarfullRecruitOwner(RecruitCarfull recruitCarfull, Member member) {
        return Objects.equals(recruitCarfull.getWriteMember().getIdNumber(), member.getIdNumber());
    }

    // 카풀에 지원한 사람이 맞는지 검증
    public boolean isCarfullRecruitApplied(Member member, long no) {
        RecruitCarfull recruitCarfull = getRecruitCarfull(no);
        return isCarfullRecruitApplied(recruitCarfull, member);
    }

    // 카풀에 지원한 사람이 맞는지 검증 2
    private boolean isCarfullRecruitApplied(RecruitCarfull recruitCarfull, Member member) {
        return applyCarfullRecruitRepository.findByRecruitCarfullAndApplicantAndCancelledFalse(recruitCarfull, member)
                .isPresent();
    }

    public ApplyRecruitCarfull getCarfullRecruitApply(RecruitCarfull recruitCarfull, Member member) {
        return applyCarfullRecruitRepository.findByRecruitCarfullAndApplicantAndCancelledFalse(recruitCarfull, member)
                .orElseThrow(CarfullRecruitNotFoundException::new);
    }

    public boolean isCarfullRecruitAccepted(Member member, long no) {
        return applyCarfullRecruitRepository.findByRecruitCarfullAndApplicantAndCancelledFalse(
                        getRecruitCarfull(no), member)
                .orElseThrow(CarfullRecruitApplyNotfoundException::new).isAccepted();
    }

    private RecruitCarfull getRecruitCarfull(long no) {
        RecruitCarfull recruitCarfull = carfullRecruitRepository.findById(no).orElseThrow(CarfullRecruitNotFoundException::new);
        if (recruitCarfull.isDeleted()) throw new CarfullRecruitDeletedException();
        return recruitCarfull;
    }
}
