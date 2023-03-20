package anu.ice.WithCar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private long writeMemberID;
    private String boardTitle;
    private int fee;
    private String startPoint;
    private String endPoint;
    private String comment;
    private int view;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime recruitWriteTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startTime;

    public RecruitCarfull(Member member, writeRecruitCarfullForm form) {
        this.writeMemberID = member.getIdNumber();
        this.boardTitle = form.getBoardTitle();
        this.fee = form.getFee();
        this.startPoint = form.getStartPoint();
        this.endPoint = form.getEndPoint();
        this.comment = form.getComment();
        this.startTime = form.getStartTime();
    }
}
