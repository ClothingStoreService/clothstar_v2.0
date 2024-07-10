package org.store.clothstar.member.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.service.MemberService;

@Service
@Slf4j
public class MemberServiceApplication {
    private final MemberService memberService;

    public MemberServiceApplication(MemberService memberService) {
        this.memberService = memberService;
    }

    public Page<MemberResponse> getAllMemberOffsetPaging(Pageable pageable) {
        return memberService.getAllMemberOffsetPaging(pageable);
    }

    public Slice<MemberResponse> getAllMemberSlicePaging(Pageable pageable) {
        return memberService.getAllMemberSlicePaging(pageable);
    }

    public MemberResponse getMemberById(Long memberId) {
        return memberService.getMemberById(memberId);
    }

    public void emailCheck(String email) {
        memberService.getMemberByEmail(email);
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

    public void signupCertifyNumEmailSend(String email) {
        memberService.signupCertifyNumEmailSend(email);
    }
}