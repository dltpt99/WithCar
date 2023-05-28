package anu.ice.WithCar.repository;

import anu.ice.WithCar.domain.entity.ChatMessageForRecruit;
import anu.ice.WithCar.domain.entity.ChatRoomForRecruit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageForRecruitRepository extends JpaRepository<ChatMessageForRecruit, Long> {
    List<ChatMessageForRecruit> findAllByRoom(ChatRoomForRecruit room);
}
