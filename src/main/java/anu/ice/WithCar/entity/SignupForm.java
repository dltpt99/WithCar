package anu.ice.WithCar.entity;

import lombok.Data;

@Data
public class SignupForm {
    String id;
    String pw;
    String pw_check;
    String nick;
    String email;
    String phone_number;
}
