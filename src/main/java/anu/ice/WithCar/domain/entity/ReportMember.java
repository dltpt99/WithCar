package anu.ice.WithCar.domain.entity;

import anu.ice.WithCar.domain.dto.ReportMemberForm;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ReportMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "whoReport")
    private Member whoReport;
    @ManyToOne
    @JoinColumn(name = "reportedMember")
    private Member reportedMember;
    private String reason;
    LocalDateTime reportTime;

    public ReportMember(Member whoReport, Member reportedMember, String reason) {
        this.whoReport = whoReport;
        this.reportedMember = reportedMember;
        this.reason = reason;
        this.reportTime = LocalDateTime.now();
    }
}
