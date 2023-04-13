package anu.ice.WithCar.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class ApplyRecruitCarfull {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ApplyId;
    @ManyToOne
    @JoinColumn(name = "RecruitCarefulId")
    private RecruitCarfull recruitCarfull;
    @ManyToOne
    @JoinColumn(name = "applicantId")
    private Member applicant;
    private boolean cancelled;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime applyTime;

    public ApplyRecruitCarfull(RecruitCarfull recruitCarfullId, Member applicantId) {
        this.recruitCarfull = recruitCarfullId;
        this.applicant = applicantId;
        applyTime = LocalDateTime.now();
        this.cancelled = false;
    }
}
