package anu.ice.WithCar.service;

import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.exception.Member.MemberNotFoundException;
import anu.ice.WithCar.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member changePassword(Member member_, String pw) {
        Member member = getMember(member_.getIdNumber());
        member.setPw(new BCryptPasswordEncoder().encode(pw));
        return saveMemberInfo(member);
    }

    public Member getMember(long id) {
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    public List<Member> getMemberList() {
        return memberRepository.findAll();
    }

    private Member saveMemberInfo(Member member){
        return memberRepository.save(member);
    }
}
