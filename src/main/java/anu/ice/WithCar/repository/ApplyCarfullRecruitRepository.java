package anu.ice.WithCar.repository;

import anu.ice.WithCar.domain.entity.ApplyRecruitCarfull;
import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.domain.entity.RecruitCarfull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyCarfullRecruitRepository extends JpaRepository<ApplyRecruitCarfull, Long> {
//    Short countAllByRecruitCarfullAndCancelledFalse(RecruitCarfull recruitCarfull);

    // 카풀 모집글과 신청자를 인자로 신청서 불러옴
    Optional<ApplyRecruitCarfull> findByRecruitCarfullAndApplicantAndCancelledFalse(RecruitCarfull recruitCarfull,
                                                                      Member member);

    // 카풀신청이 수락된 것들을 보여줌
    List<ApplyRecruitCarfull> findAllByRecruitCarfullAndAcceptedTrue(RecruitCarfull recruitCarfull);

    // 카풀 모집에 대해 유효한(취소되지않고, 거절되지 않은) 신청서들
    List<ApplyRecruitCarfull> findAllByRecruitCarfullAndDeniedFalseAndCancelledFalse(RecruitCarfull recruitCarfull);

    //추방할때 신청자가 맞는지 검증하기 위해 사용
    Optional<ApplyRecruitCarfull> findByRecruitCarfullAndApplicantAndAcceptedTrue(RecruitCarfull recruitCarfull, Member applicant);

    //신청한 목록 중 취소된 것까지 보여줌(필터링 x)
    List<ApplyRecruitCarfull> findAllByApplicant(Member applicant);
    //신청한 목록중 취소되지 않은, 유효한 내역만 보여줌(필터링 o)
    List<ApplyRecruitCarfull> findAllByApplicantAndCancelledFalse(Member applicant);

}
