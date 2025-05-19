package org.example.eksamensprojekt3sem.Membership;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.eksamensprojekt3sem.Enums.MembershipType;


import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table (name= "membership")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long membershipId;

    @JoinColumn (name = "member_id")
    @NotNull(message = "Medlems-ID skal udfyldes")
    private Long memberId;

    @Column(name = "membership_type")
    @NotNull(message = "Medlemskabstype skal udfyldes")
    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    @Column(name = "start_date")
    @NotNull(message = "Startdato skal udfyldes")
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull(message = "Slutdato skal udfyldes")
    private LocalDate endDate;



    public Membership() {}

    public Membership(Long memberId, MembershipType membershipType, LocalDate startDate, LocalDate endDate) {
        this.memberId = memberId;
        this.membershipType = membershipType;
        this.startDate = startDate;
        this.endDate = endDate;

    }


    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
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

