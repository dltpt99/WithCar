package anu.ice.WithCar.domain.dto;

import lombok.Data;

@Data
public class SignupForm {
    String id;
    String pw;
    String pw_check;
    String nick;
    String gender;
    String email;
    String phone_number;
}
