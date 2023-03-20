package anu.ice.WithCar.repository;

import anu.ice.WithCar.entity.RecruitCarfull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarfullRecruitRepository extends JpaRepository<RecruitCarfull, Long> {
}
