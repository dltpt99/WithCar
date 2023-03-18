package anu.ice.WithCar.repository;

import anu.ice.WithCar.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUserId(String user_id);

    Optional<Member> findByNick(String nick);

}
