package anu.ice.WithCar.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenEntity {
    private String access_token;
    private String refresh_token;
}
