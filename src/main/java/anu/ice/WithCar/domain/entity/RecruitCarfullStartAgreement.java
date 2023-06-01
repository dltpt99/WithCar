package anu.ice.WithCar.domain.entity;

import anu.ice.WithCar.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class RecruitCarfullStartAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private ApplyRecruitCarfull applyRecruitCarfull;
    private boolean agree;
    private LocalDateTime agreeTime;

    public RecruitCarfullStartAgreement(ApplyRecruitCarfull apply) {
        this.applyRecruitCarfull = apply;
        this.agree = true;
        this.agreeTime = LocalDateTime.now();
    }
}
