package anu.ice.WithCar.repository;

import anu.ice.WithCar.domain.entity.ApplyRecruitCarfull;
import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.domain.entity.RecruitCarfull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplyCarfullRecruitRepository extends JpaRepository<ApplyRecruitCarfull, Long> {
//    Short countAllByRecruitCarfullAndCancelledFalse(RecruitCarfull recruitCarfull);

    Optional<ApplyRecruitCarfull> findAllByRecruitCarfullAndApplicantAndCancelledFalse(RecruitCarfull recruitCarfull,
                                                                      Member member);
}
