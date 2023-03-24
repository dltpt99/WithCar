package anu.ice.WithCar.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private long id;
    private String userId;
    private String nick;
    private String email;
    private String role;
    private TokenEntity token;

    public LoginResponse(Member member) {
        this.id = member.getIdNumber();
        this.userId = member.getUserId();
        this.nick = member.getNick();
        this.email = member.getEmail();
        this.role = member.getRole();
    }
}
