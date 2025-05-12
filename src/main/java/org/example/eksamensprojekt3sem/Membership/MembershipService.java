
package org.example.eksamensprojekt3sem.Membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.eksamensprojekt3sem.Membership.MembershipModel;
import java.time.LocalDate;
import java.util.List;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;

    @Autowired
    public MembershipService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    public List<MembershipModel> getAllMemberships(Long id) {
        return membershipRepository.findAll();
    }

    public MembershipModel getMembershipById(Long id){
        return membershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
    }

    public MembershipModel createMembership(MembershipModel membership){
        return membershipRepository.save(membership);
    }

    public MembershipModel updateMembership(Long id, LocalDate startDate, LocalDate endDate, MembershipModel updatedMembership) {
        MembershipModel existing = getMembershipById(id);
        existing.setStartDate(updatedMembership.getStartDate());
        existing.setEndDate(updatedMembership.getEndDate());
        existing.setMembershipType(updatedMembership.getMembershipType());
        return membershipRepository.save(existing);
    }

    public void deleteMembership(Long id){
        membershipRepository.deleteById(id);
    }
}
