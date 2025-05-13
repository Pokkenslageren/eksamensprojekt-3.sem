
package org.example.eksamensprojekt3sem.Membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;

    @Autowired
    public MembershipService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    public List<Membership> getAllMemberships(Long id) {
        return membershipRepository.findAll();
    }

    public Membership getMembershipById(Long id){
        return membershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
    }

    public Membership createMembership(Membership membership){
        return membershipRepository.save(membership);
    }

    public Membership updateMembership(Long id, LocalDate startDate, LocalDate endDate, Membership updatedMembership) {
        Membership existing = getMembershipById(id);
        existing.setStartDate(updatedMembership.getStartDate());
        existing.setEndDate(updatedMembership.getEndDate());
        existing.setMembershipType(updatedMembership.getMembershipType());
        return membershipRepository.save(existing);
    }

    public void deleteMembership(Long id){
        membershipRepository.deleteById(id);
    }
}
