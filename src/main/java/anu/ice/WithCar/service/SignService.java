package anu.ice.WithCar.service;

import anu.ice.WithCar.entity.Member;
import anu.ice.WithCar.entity.SignupForm;
import anu.ice.WithCar.entity.UserDetailsEntity;
import anu.ice.WithCar.exception.NickDuplicateException;
import anu.ice.WithCar.exception.UserIdDuplicateException;
import anu.ice.WithCar.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SignService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Autowired
    public SignService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void signup_service(SignupForm form) {
        form.setPw(
                new BCryptPasswordEncoder().encode(form.getPw()
                ));
        //중복 id, nick 검사
        check_duplicate_user_id(form);
        check_duplicate_nick(form);

        Member member = new Member(form);
        memberRepository.save(member);

    }

    private void check_duplicate_user_id(SignupForm form) {
        Optional<Member> member = memberRepository.findByUserId(form.getId());
        if (member.isPresent()) {
            throw new UserIdDuplicateException();
        }
    }

    private void check_duplicate_nick(SignupForm form) {
        Optional<Member> member = memberRepository.findByNick(form.getId());
        if (member.isPresent()) {
            throw new NickDuplicateException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(()-> new UsernameNotFoundException("등록되지 않은 사용자 입니다"));

        return new UserDetailsEntity(member);
    }
}
