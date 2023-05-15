package anu.ice.WithCar.service;

import anu.ice.WithCar.domain.*;
import anu.ice.WithCar.domain.dto.LoginRequest;
import anu.ice.WithCar.domain.dto.LoginResponse;
import anu.ice.WithCar.domain.dto.SignupForm;
import anu.ice.WithCar.domain.dto.UserDetailsEntity;
import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.exception.Member.NickDuplicateException;
import anu.ice.WithCar.exception.NotLoginException;
import anu.ice.WithCar.exception.Member.UserIdDuplicateException;
import anu.ice.WithCar.repository.MemberRepository;
import anu.ice.WithCar.repository.TokenRepository;
import anu.ice.WithCar.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class SignService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtProvider;

    @Autowired
    public SignService(MemberRepository memberRepository, TokenRepository tokenRepository, JwtTokenProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.tokenRepository = tokenRepository;
        this.jwtProvider = jwtProvider;
    }

    public void signup_service(SignupForm form) {
        form.setPw(
                new BCryptPasswordEncoder().encode(form.getPw()
                ));
        //중복 id, nick 검사
        check_duplicate_user_id(form.getId());
        check_duplicate_nick(form.getNick());

        Member member = new Member(form);
        memberRepository.save(member);

    }

    public void check_duplicate_user_id(String user_id) {
        Optional<Member> member = memberRepository.findByUserId(user_id);
        if (member.isPresent()) {
            throw new UserIdDuplicateException();
        }
    }

    public void check_duplicate_nick(String nick) {
        Optional<Member> member = memberRepository.findByNick(nick);
        if (member.isPresent()) {
            throw new NickDuplicateException();
        }
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUserId(request.getUserId()).orElseThrow(() ->
                new BadCredentialsException("wrongAccountInformation"));

        if (!new BCryptPasswordEncoder().matches(request.getPw(), member.getPw())) {
            throw new BadCredentialsException("wrongAccountInformation");
        }

        member.setRefreshToken(createRefreshToken(member));
        LoginResponse response = new LoginResponse(member);
        TokenEntity token = TokenEntity.builder()
                .access_token(jwtProvider.createToken(member.getUserId(), member.getRole()))
                .refresh_token(member.getRefreshToken())
                .build();

        response.setToken(token);
        return response;
    }

    public LoginResponse getMember(String userId) throws Exception {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("AccountNotFound"));
        return new LoginResponse(member);
    }

    public String createRefreshToken(Member member) {
        Token token = tokenRepository.save(
                Token.builder()
                        .id(member.getIdNumber())
                        .refresh_token(UUID.randomUUID().toString())
                        .expiration(LocalDateTime.now())
                        .build()
        );
        return token.getRefresh_token();
    }

    public Token validRefreshToken(Member member, String refreshToken) throws Exception {
        Token token = tokenRepository.findById(member.getIdNumber()).orElseThrow(() -> new Exception("expired"));
        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefresh_token() == null) {
            return null;
        } else {
            // 30분 지나면 폐기
            if(ChronoUnit.MINUTES.between(token.getExpiration(), LocalDateTime.now()) > 30) {
                tokenRepository.delete(token);
                return null;
            }
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장..?
            // 20분을 지나고, 30분은 안지났을 때.
            if(ChronoUnit.MINUTES.between(token.getExpiration(), LocalDateTime.now()) < 20) {
                token.setExpiration(LocalDateTime.now());
                tokenRepository.save(token);
            }

            // 토큰이 같은지 비교
            if(!token.getRefresh_token().equals(refreshToken)) {
                return null;
            } else {
                return token;
            }
        }
    }

    public TokenEntity refreshAccessToken(TokenEntity token) throws Exception {
        String account = jwtProvider.getUserId(token.getAccess_token());
        Member member = memberRepository.findByUserId(account).orElseThrow(() ->
                new BadCredentialsException("wrongAccountInformation"));
        Token refreshToken = validRefreshToken(member, token.getRefresh_token());

        if (refreshToken != null) {
            return TokenEntity.builder()
                    .access_token(jwtProvider.createToken(account, member.getRole()))
                    .refresh_token(refreshToken.getRefresh_token())
                    .build();
        } else {
            throw new NotLoginException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(()-> new UsernameNotFoundException("UserNotFoundError"));

        return new UserDetailsEntity(member);
    }
}
