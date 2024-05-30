package org.store.clothstar.member.service;

import org.store.clothstar.member.dto.request.CreateMemberRequest;

public interface MemberSignupService {
    Long signUp(CreateMemberRequest createMemberDTO);
}