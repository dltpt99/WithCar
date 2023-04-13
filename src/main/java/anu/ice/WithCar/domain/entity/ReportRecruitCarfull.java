package anu.ice.WithCar.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ReportRecruitCarfull {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "whoReport")
    private Member whoReport;
    @ManyToOne
    @JoinColumn(name = "reportedRecruitCarfull")
    private RecruitCarfull reportedRecruitCarfull;
    private String reason;
    LocalDateTime reportTime;

    public ReportRecruitCarfull(Member whoReport, RecruitCarfull reportedRecruitCarfull, String reason) {
        this.whoReport = whoReport;
        this.reportedRecruitCarfull = reportedRecruitCarfull;
        this.reason = reason;
        this.reportTime = LocalDateTime.now();
    }
}
