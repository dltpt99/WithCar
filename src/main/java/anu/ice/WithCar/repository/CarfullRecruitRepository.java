package anu.ice.WithCar.repository;

import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.domain.entity.RecruitCarfull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarfullRecruitRepository extends JpaRepository<RecruitCarfull, Long> {
    List<RecruitCarfull> findAllByDeletedFalse();
    List<RecruitCarfull> findAllByDeletedFalseAndWriteMember(Member writerMember);

}
