package anu.ice.WithCar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
    private String startPoint;
    private String endPoint;
    private String comment;
    private int view;
    private boolean isDeleted;

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
        this.isDeleted = false;
    }

}
