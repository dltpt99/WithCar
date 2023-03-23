package anu.ice.WithCar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class ApplyRaecruitCarfull {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ApplyId;
    @ManyToOne
    @JoinColumn(name = "RecruitCarfullId")
    private RecruitCarfull recruitCarfull;
    @ManyToOne
    @JoinColumn(name = "applicantId")
    private Member applicant;
    private boolean isCancelled;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime applyTime;

    public ApplyRaecruitCarfull(RecruitCarfull recruitCarfullId, Member applicantId) {
        this.recruitCarfull = recruitCarfullId;
        this.applicant = applicantId;
        applyTime = LocalDateTime.now();
        this.isCancelled = false;
    }
}
