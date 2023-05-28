package anu.ice.WithCar.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ChatRoomForRecruit {
    @Id
    private long id;

    @OneToOne
    @JoinColumn(name = "recruitID")
    private RecruitCarfull recruitCarfull;

//    @ElementCollection
//    @CollectionTable(name = "chatRoomForRecruitUsers")
    @OneToMany
    private List<Member> users;
    private enum state {
        ENABLE, DISABLE
    }

    public ChatRoomForRecruit(RecruitCarfull recruit) {
        recruitCarfull = recruit;

        this.users = new ArrayList<>();
        users.add(recruit.getWriteMember());
    }
}
