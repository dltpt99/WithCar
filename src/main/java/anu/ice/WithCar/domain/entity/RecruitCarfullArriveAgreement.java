package anu.ice.WithCar.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class RecruitCarfullArriveAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private ApplyRecruitCarfull applyRecruitCarfull;
    private boolean agree;
    private LocalDateTime agreeTime;

    public RecruitCarfullArriveAgreement(ApplyRecruitCarfull apply) {
        this.applyRecruitCarfull = apply;
        this.agree = true;
        this.agreeTime = LocalDateTime.now();
    }
}