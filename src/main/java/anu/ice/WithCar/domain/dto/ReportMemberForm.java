package anu.ice.WithCar.domain.dto;

import anu.ice.WithCar.domain.entity.Member;
import lombok.Data;

@Data
public class ReportMemberForm {
    private long whoReport;
    private long reportedMember;
    private String reason;
}
