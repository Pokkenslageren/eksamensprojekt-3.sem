package org.example.eksamensprojekt3sem.Membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.eksamensprojekt3sem.Membership.MembershipService;

import java.util.List;

@RestController
@RequestMapping()
public class MembershipController {

    private MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping("/{id}")
    public List<MembershipModel> getAllMemberships(@PathVariable Long id) {
        return membershipService.getAllMemberships(id);
    }

    @PostMapping
    public MembershipModel createMembership(@RequestBody MembershipModel membershipModel) {
        return membershipService.createMembership(membershipModel);
    }

    @PutMapping("/{id}")
    public MembershipModel updateMembership(@PathVariable Long id, @RequestBody MembershipModel membershipModel) {
        return membershipService.updateMembership(id, membershipModel.getStartDate(), membershipModel.getEndDate(), membershipModel);
    }

    @DeleteMapping("/{id}")
    public void deleteMembership(@PathVariable Long id) {
        membershipService.deleteMembership(id);
    }
}
