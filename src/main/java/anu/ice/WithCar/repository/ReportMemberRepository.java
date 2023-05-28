package anu.ice.WithCar.repository;

import anu.ice.WithCar.domain.entity.ReportMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportMemberRepository extends JpaRepository<ReportMember, Long> {
}
