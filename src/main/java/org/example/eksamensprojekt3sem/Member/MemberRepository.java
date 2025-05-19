package org.example.eksamensprojekt3sem.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //Find member by name
    List<Member> findByNameContainingIgnoreCase(String name);
    //Find member by email
    List<Member> findByEmailContainingIgnoreCase(String email);
    //Find member by Paymentstatus
    List<Member> findByPaymentStatus(String paymentStatus);

}
