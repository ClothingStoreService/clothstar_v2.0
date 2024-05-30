package org.store.clothstar.member.service;

import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;

import java.util.List;

public interface MemberBasicService {
    List<MemberResponse> findAll();

    MemberResponse findById(Long memberId);

    boolean findByEmail(String email);

    void update(Long memberId, ModifyMemberRequest modifyMemberRequest);
}
