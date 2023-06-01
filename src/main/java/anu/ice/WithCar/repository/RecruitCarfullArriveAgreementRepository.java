package anu.ice.WithCar.repository;

import anu.ice.WithCar.domain.entity.ApplyRecruitCarfull;
import anu.ice.WithCar.domain.entity.RecruitCarfullArriveAgreement;
import anu.ice.WithCar.domain.entity.RecruitCarfullStartAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruitCarfullArriveAgreementRepository extends JpaRepository<RecruitCarfullArriveAgreement, Long> {

//    List<RecruitCarfullArriveAgreement> findAllByApplyRecruitCarfull(ApplyRecruitCarfull applyRecruitCarfull);

    Optional<RecruitCarfullArriveAgreement> findByApplyRecruitCarfull(ApplyRecruitCarfull applyRecruitCarfull);
}