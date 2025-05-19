package org.example.eksamensprojekt3sem.Member;

import org.example.eksamensprojekt3sem.Enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberRepository memberRepository;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        memberService = new MemberService(memberRepository);
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        // Arrange
        Member member1 = new Member("Anna Hansen", 1L, "anna@example.com", "12345678", "Vej 1", new Date(), PaymentStatus.PAID);
        Member member2 = new Member("Johanna Sørensen", 2L, "johanna@example.com", "87654321", "Vej 2", new Date(), PaymentStatus.UNPAID);

        List<Member> mockResult = Arrays.asList(member1, member2);
        when(memberRepository.findByNameContainingIgnoreCase("anna")).thenReturn(mockResult);

        // Act
        List<Member> result = memberService.findByNameContainingIgnoreCase("anna");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(member1));
        assertTrue(result.contains(member2));
        verify(memberRepository, times(1)).findByNameContainingIgnoreCase("anna");
    }
}
