package anu.ice.WithCar.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ChatMessageForRecruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "chatRoomID")
    private ChatRoomForRecruit room;

    @ManyToOne
    @JoinColumn(name = "sendMemberID")
    private Member sender;
    public enum MessageType {
        ENTER, LEAVE, TEXT
    }

    private MessageType type;

    private String msgText;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime sendTime;

}
