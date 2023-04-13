package anu.ice.WithCar.controller;

import anu.ice.WithCar.domain.dto.ReportMemberForm;
import anu.ice.WithCar.domain.dto.ReportRecruitCarfullForm;
import anu.ice.WithCar.domain.entity.ReportMember;
import anu.ice.WithCar.domain.entity.ReportRecruitCarfull;
import anu.ice.WithCar.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/report/member")
    public String reportMemberController(ReportMemberForm form) {
        reportService.reportMember(form);
        return "0";
    }

    @PostMapping("/report/recruitcarfull")
    public String reportRecruitCarfullController(ReportRecruitCarfullForm form) {
        reportService.reportRecruitCarfull(form);
        return "0";
    }

    @GetMapping("/report/list/member")
    public List<ReportMember> showReportMemberList() {
        return reportService.showMemberReports();
    }

    @GetMapping("/report/list/recruit")
    public List<ReportRecruitCarfull> showReportRecruitCarfullList() {
        return reportService.showRecruitCarfullReports();
    }
}
