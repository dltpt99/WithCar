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
//    @CollectionTable(name = "chatrromforrecruit_users", joinColumns = )
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany
    @JoinColumn(name = "id")
    private List<Member> users = new ArrayList<>();
    private enum state {
        ENABLE, DISABLE
    }

    public ChatRoomForRecruit(RecruitCarfull recruit) {
        recruitCarfull = recruit;
        users.add(recruit.getWriteMember());
    }
}
