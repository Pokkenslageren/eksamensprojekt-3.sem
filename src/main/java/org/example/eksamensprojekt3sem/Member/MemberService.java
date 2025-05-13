package org.example.eksamensprojekt3sem.Member;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(long id) {
        if(id <= 0){
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return memberRepository.findById(id);
    }

    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> updateMember(long id, @Valid Member memberDetails) {
        if (id <= 0){
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return memberRepository.findById(id).map(member1 -> {
            member1.setName(memberDetails.getName());
            member1.setEmail(memberDetails.getEmail());
            member1.setPhone(memberDetails.getPhone());
            member1.setAddress(memberDetails.getAddress());
            member1.setDateOfBirth(memberDetails.getDateOfBirth());
            return memberRepository.save(member1);
        });
    }

    public boolean deleteMember(long id) {
        if (id <= 0){
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        if(memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /*
    public List<MembershipModel> getMemberships(){
        return membershipRepository.findAll();
    }
    */
}
