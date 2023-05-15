package anu.ice.WithCar.repository;

import anu.ice.WithCar.domain.entity.ChatRoomForRecruit;
import anu.ice.WithCar.domain.entity.RecruitCarfull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomForRecruitRepository extends JpaRepository<ChatRoomForRecruit, Long> {
    Optional<ChatRoomForRecruit> findByRecruitCarfull(RecruitCarfull recruitCarfull);

}
