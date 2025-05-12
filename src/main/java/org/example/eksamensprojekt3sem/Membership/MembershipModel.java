package org.example.eksamensprojekt3sem.Membership;

import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table (name= "membership")
public class MembershipModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long membershipId;

    @JoinColumn (name = "member_id")
    private Member member;

    @Column
    private String membershipType;

    private LocalDate startDate;

    private LocalDate endDate;

    //@OneToMany(mappedBy = "memberShip")
    private List<Payment> payments;

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

}

