package anu.ice.WithCar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecureConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/**").permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/login").permitAll()

                );
        http.cors((cors) -> cors.disable());


        return http.build();
    }

}
