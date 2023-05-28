package anu.ice.WithCar.domain.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String pw;
}
