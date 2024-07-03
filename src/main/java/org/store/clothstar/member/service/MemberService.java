package org.store.clothstar.member.service;

import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;

import java.util.List;

public interface MemberService {
    List<MemberResponse> findAll();

    MemberResponse getMemberById(Long memberId);

    boolean getMemberByEmail(String email);

    void updateDeleteAt(Long memberId);

    void updatePassword(Long memberId, String password);

    void modifyMember(Long memberId, ModifyMemberRequest modifyMemberRequest);

    Long signUp(CreateMemberRequest createMemberDTO);
}
