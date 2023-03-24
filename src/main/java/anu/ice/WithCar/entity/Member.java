package anu.ice.WithCar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "member")
@Data
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idNumber;
    @Column(unique = true)
    String userId;
    String pw;
    @Column(unique = true)
    String nick;
    String email;
    String phoneNumber;
    String gender;
    String role;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime signDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate lastPwChangedDate;
    @Transient
    private String refreshToken;

    public Member(SignupForm form) {
        userId = form.getId();
        pw = form.getPw();
        nick = form.getNick();
        email = form.getEmail();
        phoneNumber = form.getPhone_number();
        signDate = LocalDateTime.now();
        lastPwChangedDate = LocalDate.now();
        role = "ROLE_USER";
    }
}
