package org.store.clothstar.member.service;

import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;

import java.util.List;

public interface MemberBasicService {
    List<MemberResponse> findAll();

    MemberResponse getMemberById(Long memberId);

    boolean getMemberByEmail(String email);

    void modifyMember(Long memberId, ModifyMemberRequest modifyMemberRequest);
}
