package org.example.eksamensprojekt3sem.Membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<Membership> getAllMemberships(@PathVariable Long id) {
        return membershipService.getAllMemberships(id);
    }

    @PostMapping
    public Membership createMembership(@RequestBody Membership membership) {
        return membershipService.createMembership(membership);
    }

    @PutMapping("/{id}")
    public Membership updateMembership(@PathVariable Long id, @RequestBody Membership membership) {
        return membershipService.updateMembership(id, membership.getStartDate(), membership.getEndDate(), membership);
    }

    @DeleteMapping("/{id}")
    public void deleteMembership(@PathVariable Long id) {
        membershipService.deleteMembership(id);
    }
}
