package anu.ice.WithCar.service;

import anu.ice.WithCar.entity.Member;
import anu.ice.WithCar.entity.SignupForm;
import anu.ice.WithCar.exception.NickDuplicateException;
import anu.ice.WithCar.exception.UserIdDuplicateException;
import anu.ice.WithCar.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SignService {
    private final MemberRepository memberRepository;

    @Autowired
    public SignService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void signup_service(SignupForm form) {
        //중복 id, nick 검사
        check_duplicate_user_id(form);
        check_duplicate_nick(form);

        Member member = new Member(form);
        memberRepository.save(member);

    }

    private void check_duplicate_user_id(SignupForm form) {
        Optional<Member> member = memberRepository.findByUserId(form.getId());
        member.orElseThrow(UserIdDuplicateException::new);
    }

    private void check_duplicate_nick(SignupForm form) {
        Optional<Member> member = memberRepository.findByNick(form.getId());
        member.orElseThrow(NickDuplicateException::new);
    }
}
