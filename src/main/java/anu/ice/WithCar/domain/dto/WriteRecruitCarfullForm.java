package anu.ice.WithCar.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class WriteRecruitCarfullForm {
    private String boardTitle;
    private int fee;
    private short personLimit;
    private String startPoint;
    private String endPoint;
    private String comment;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startTime;
}
