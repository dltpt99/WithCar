package anu.ice.WithCar.entity;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String pw;
}
