package anu.ice.WithCar.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class EditRecruitCarfullForm {
    private long recruitCarfullID;
    private String boardTitle;
    private int fee;
    private String startPoint;
    private String endPoint;
    private String comment;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startTime;
}
