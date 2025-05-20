package org.example.eksamensprojekt3sem.Member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eksamensprojekt3sem.Enums.PaymentStatus;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUpdatePaymentStatus_Success() throws Exception {
        long memberId = 1L;
        PaymentStatusDTO request = new PaymentStatusDTO();
        request.setStatus("PAID");

        Member mockMember = new Member();
        mockMember.setMemberId(memberId);
        mockMember.setPaymentStatus(PaymentStatus.PAID);

        Mockito.when(memberService.setPaymentStatus(memberId, PaymentStatus.PAID))
                .thenReturn(Optional.of(mockMember));

        mockMvc.perform(put("/members/{id}/payment-status", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.paymentStatus").value("PAID"));
    }

    @Test
    void testUpdatePaymentStatus_InvalidStatus() throws Exception {
        long memberId = 1L;
        String invalidPayload = "{\"status\": \"INVALID_ENUM\"}";

        mockMvc.perform(put("/members/{id}/payment-status", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePaymentStatus_MemberNotFound() throws Exception {
        long memberId = 99L;
        PaymentStatusDTO request = new PaymentStatusDTO();
        request.setStatus("PAID");

        Mockito.when(memberService.setPaymentStatus(memberId, PaymentStatus.PAID))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/members/{id}/payment-status", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
