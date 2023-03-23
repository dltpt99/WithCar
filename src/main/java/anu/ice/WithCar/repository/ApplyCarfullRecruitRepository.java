package anu.ice.WithCar.repository;

import anu.ice.WithCar.entity.ApplyRaecruitCarfull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyCarfullRecruitRepository extends JpaRepository<ApplyRaecruitCarfull, Long> {

}
