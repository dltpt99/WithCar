package anu.ice.WithCar.repository;

import anu.ice.WithCar.domain.entity.RecruitCarfullStartAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import anu.ice.WithCar.domain.entity.ApplyRecruitCarfull;

import java.util.List;
import java.util.Optional;

public interface RecruitCarfullStartAgreementRepository extends JpaRepository<RecruitCarfullStartAgreement, Long> {

//    List<RecruitCarfullStartAgreement> findAllByApplyRecruitCarfull(ApplyRecruitCarfull applyRecruitCarfull);

    Optional<RecruitCarfullStartAgreement> findByApplyRecruitCarfull(ApplyRecruitCarfull applyRecruitCarfull);
}
