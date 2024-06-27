package org.store.clothstar.member.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.service.MemberService;

import java.util.List;

@Service
@Slf4j
public class MemberServiceApplication {
    private final MemberService memberService;

    public MemberServiceApplication(MemberService memberService) {
        this.memberService = memberService;
    }

    public List<MemberResponse> getAllMember() {
        return memberService.findAll();
    }

    public MemberResponse getMemberById(Long memberId) {
        return memberService.getMemberById(memberId);
    }

    public boolean emailCheck(String email) {
        return memberService.getMemberByEmail(email);
    }

    public void modifyMember(Long memberId, ModifyMemberRequest modifyMemberRequest) {
        memberService.modifyMember(memberId, modifyMemberRequest);
    }

    public void modifyPassword(Long memberId, String password) {
        memberService.updatePassword(memberId, password);
    }

    public void updateDeleteAt(Long memberId) {
        memberService.updateDeleteAt(memberId);
    }

    public Long signup(CreateMemberRequest createMemberDTO) {
        return memberService.signUp(createMemberDTO);
    }

    public void signupEmailAuthentication(Long memberId) {
        memberService.signupEmailAuthentication(memberId);
    }
}