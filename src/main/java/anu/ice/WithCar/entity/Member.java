package anu.ice.WithCar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    Long id_number;
    String userId;
    String pw;
    String nick;
    String email;
    String phoneNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime sign_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate last_pw_changed_date;

    public Member(SignupForm form) {
        userId = form.getId();
        pw = form.getPw();
        nick = form.getNick();
        email = form.getEmail();
        phoneNumber = form.getPhone_number();
        sign_date = LocalDateTime.now();
    }

}
