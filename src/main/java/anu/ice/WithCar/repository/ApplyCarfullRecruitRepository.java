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

    Optional<ApplyRecruitCarfull> findAllByRecruitCarfullAndApplicantAndCancelledFalse(RecruitCarfull recruitCarfull,
                                                                      Member member);

    //신청한 목록 중 취소된 것까지 보여줌(필터링 x)
    List<ApplyRecruitCarfull> findAllByApplicant(Member applicant);
    //신청한 목록중 취소되지 않은, 유효한 내역만 보여줌(필터링 o)
    List<ApplyRecruitCarfull> findAllByApplicantAndCancelledFalse(Member applicant);

}
