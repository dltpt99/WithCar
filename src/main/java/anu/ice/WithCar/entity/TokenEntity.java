package anu.ice.WithCar.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenEntity {
    private String access_token;
    private String refresh_token;
}
