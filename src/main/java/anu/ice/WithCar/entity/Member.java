package anu.ice.WithCar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "member")
@Data
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idNumber;
    String userId;
    String pw;
    String nick;
    String email;
    String phoneNumber;
    Role role;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime signDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate lastPwChangedDate;

    public Member(SignupForm form) {
        userId = form.getId();
        pw = form.getPw();
        nick = form.getNick();
        email = form.getEmail();
        phoneNumber = form.getPhone_number();
        signDate = LocalDateTime.now();
        lastPwChangedDate = LocalDate.now();
    }



}
