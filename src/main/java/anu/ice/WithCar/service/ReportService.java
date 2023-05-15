package anu.ice.WithCar.service;

import anu.ice.WithCar.domain.dto.ReportMemberForm;
import anu.ice.WithCar.domain.dto.ReportRecruitCarfullForm;
import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.domain.entity.RecruitCarfull;
import anu.ice.WithCar.domain.entity.ReportMember;
import anu.ice.WithCar.domain.entity.ReportRecruitCarfull;
import anu.ice.WithCar.exception.CarfullRecruit.CarfullRecruitNotFoundException;
import anu.ice.WithCar.exception.Member.MemberNotFoundException;
import anu.ice.WithCar.repository.CarfullRecruitRepository;
import anu.ice.WithCar.repository.MemberRepository;
import anu.ice.WithCar.repository.ReportMemberRepository;
import anu.ice.WithCar.repository.ReportRecruitCarfullRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final ReportMemberRepository reportMemberRepository;
    private final ReportRecruitCarfullRepository reportRecruitCarfullRepository;
    private final MemberRepository memberRepository;
    private final CarfullRecruitRepository carfullRecruitRepository;

    @Autowired
    public ReportService(ReportMemberRepository reportMemberRepository, ReportRecruitCarfullRepository reportRecruitCarfullRepository, MemberRepository memberRepository, CarfullRecruitRepository carfullRecruitRepository) {
        this.reportMemberRepository = reportMemberRepository;
        this.reportRecruitCarfullRepository = reportRecruitCarfullRepository;
        this.memberRepository = memberRepository;
        this.carfullRecruitRepository = carfullRecruitRepository;
    }

    public void reportMember(ReportMemberForm form) {
        Member whoReport = memberRepository.findById(form.getWhoReport()).orElseThrow(MemberNotFoundException::new);
        Member reportedMember = memberRepository.findById(form.getReportedMember()).orElseThrow(MemberNotFoundException::new);

        ReportMember reportMember = new ReportMember(whoReport, reportedMember, form.getReason());
        reportMemberRepository.save(reportMember);
    }

    public void reportRecruitCarfull(ReportRecruitCarfullForm form) {
        Member whoReport = memberRepository.findById(form.getWhoReport()).orElseThrow(MemberNotFoundException::new);
        RecruitCarfull recruitCarfull = carfullRecruitRepository.findById(form.getReportedRecruitCarfull()).orElseThrow(CarfullRecruitNotFoundException::new);

        ReportRecruitCarfull reportRecruitCarfull = new ReportRecruitCarfull(whoReport, recruitCarfull, form.getReason());
        reportRecruitCarfullRepository.save(reportRecruitCarfull);
    }

    public List<ReportMember> showMemberReports() {
        return reportMemberRepository.findAll();
    }

    public List<ReportRecruitCarfull> showRecruitCarfullReports() {
        return reportRecruitCarfullRepository.findAll();
    }
}
