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

    public List<MemberModel> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<MemberModel> getMemberById(long id) {
        if(id <= 0){
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return memberRepository.findById(id);
    }

    public MemberModel addMember(MemberModel member) {
        return memberRepository.save(member);
    }

    public Optional<MemberModel> updateMember(long id, @Valid MemberModel memberDetails) {
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
