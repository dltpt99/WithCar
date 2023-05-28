package anu.ice.WithCar.domain.entity;

import anu.ice.WithCar.domain.dto.WriteRecruitCarfullForm;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "recruitCarfull")
@Data
@NoArgsConstructor
public class RecruitCarfull{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recruitCarfullID;
    @ManyToOne
    @JoinColumn(name = "writeMemberID")
    private Member writeMember;
    private String boardTitle;
    private int fee;
    private short personLimit;
    private short applyPersonCount;
    private String startPoint;
    private String endPoint;
    private String comment;
    private int view;
    private boolean needStartAgree;
    private boolean started;
    private boolean needArriveAgree;
    private boolean arrived;
    private boolean deleted;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime recruitWriteTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime startTime;

    public RecruitCarfull(Member member, WriteRecruitCarfullForm form) {
        this.writeMember = member;
        this.boardTitle = form.getBoardTitle();
        this.fee = form.getFee();
        this.personLimit = form.getPersonLimit();
        this.startPoint = form.getStartPoint();
        this.endPoint = form.getEndPoint();
        this.comment = form.getComment();
        this.startTime = form.getStartTime();
        this.recruitWriteTime = LocalDateTime.now();
        this.applyPersonCount = 0;
        this.view = 0;
        this.deleted = false;
        this.started = false;
        this.needStartAgree = false;
        this.arrived = false;
        this.needArriveAgree = false;
    }
//
//    public void startAgree(Member member) {
//        RecruitCarfullStartAgreement agreement = new RecruitCarfullStartAgreement(member);
//        startAgreement.put(member, agreement);
//    }
//
//    public void arriveAgree(Member member) {
//        RecruitCarfullArriveAgreement agreement = new RecruitCarfullArriveAgreement(member);
//        arriveAgreement.put(member, agreement);
//    }

    public void applyCountUp() {
        this.applyPersonCount++;
    }

    public void applyCountDown() {
        this.applyPersonCount--;
    }

    public void increaseView() {
        this.view++;
    }

}
