package org.example.eksamensprojekt3sem.Member;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberModel> getAllMembers() {
        return memberRepository.findAll();
    }
}
