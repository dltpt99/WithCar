package anu.ice.WithCar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "loginToken")
public class Token {
    @Id
    @JsonIgnore
    private Long id;

    private String refresh_token;
    private LocalDateTime expiration;

}
