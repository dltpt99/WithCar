package anu.ice.WithCar.service;

import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getMemberList() {
        return memberRepository.findAll();
    }

}
