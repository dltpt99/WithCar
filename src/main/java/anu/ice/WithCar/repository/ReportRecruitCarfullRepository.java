package anu.ice.WithCar.repository;

import anu.ice.WithCar.domain.entity.ReportRecruitCarfull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRecruitCarfullRepository extends JpaRepository<ReportRecruitCarfull, Long> {
}
