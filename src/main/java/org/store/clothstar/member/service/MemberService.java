package org.store.clothstar.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;

public interface MemberService {
    Page<MemberResponse> getAllMemberOffsetPaging(Pageable pageable);

    Slice<MemberResponse> getAllMemberSlicePaging(Pageable pageable);

    MemberResponse getMemberById(Long memberId);

    boolean getMemberByEmail(String email);

    void updateDeleteAt(Long memberId);

    void updatePassword(Long memberId, String password);

    void modifyMember(Long memberId, ModifyMemberRequest modifyMemberRequest);

    Long signUp(CreateMemberRequest createMemberDTO);

    void signupCertifyNumEmailSend(String email);
}