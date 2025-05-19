package org.example.eksamensprojekt3sem.Member;

import jakarta.validation.Valid;
import org.example.eksamensprojekt3sem.Enums.PaymentStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.List;

@RestController
@RequestMapping("")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        System.out.println("MemberController");
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/members/add")
    public Member createMember(@Valid @RequestBody Member member) {
        return memberService.addMember(member);
    }

    @PutMapping("/members/update/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable long id, @Valid @RequestBody Member memberDetails) {
        return memberService.updateMember(id, memberDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/members/delete/{id}")
    public ResponseEntity<Member> deleteMember(@PathVariable long id) {
        if (memberService.deleteMember(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/member/search/name")
    public List<Member> findByNameContainingIgnoreCase(@RequestParam String name){
        return memberService.findByNameContainingIgnoreCase(name);
    }
    @GetMapping("/member/search/email")
    public List<Member> findByEmailContainingIgnoreCase(@RequestParam String email){
        return memberService.findByEmailContainingIgnoreCase(email);
    }

    @GetMapping("/member/search/paymentstatus")
    public List<Member> findByPaymentStatus(@RequestParam PaymentStatus paymentstatus){
        return memberService.findByPaymentStatus(paymentstatus);
    }
}
