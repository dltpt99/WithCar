package anu.ice.WithCar.domain.dto;

import lombok.Data;

@Data
public class ReportRecruitCarfullForm {
    private long whoReport;
    private long reportedRecruitCarfull;
    private String reason;
}
