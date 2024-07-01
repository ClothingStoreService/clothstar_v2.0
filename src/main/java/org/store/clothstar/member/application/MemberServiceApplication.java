package org.store.clothstar.member.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.service.MemberBasicService;
import org.store.clothstar.member.service.MemberDeleteService;
import org.store.clothstar.member.service.MemberPasswordUpdateService;
import org.store.clothstar.member.service.MemberSignupService;

import java.util.List;

@Service
@Slf4j
public class MemberServiceApplication {
    private final MemberBasicService memberBasicService;
    private final MemberPasswordUpdateService memberPasswordUpdateService;
    private final MemberSignupService memberSignupService;
    private final MemberDeleteService memberDeleteService;

    public MemberServiceApplication(MemberBasicService memberBasicService,
                                    MemberPasswordUpdateService memberPasswordUpdateService,
                                    @Qualifier("memberSignupJpaServiceImpl") MemberSignupService memberSignupService,
                                    MemberDeleteService memberDeleteService) {
        this.memberBasicService = memberBasicService;
        this.memberPasswordUpdateService = memberPasswordUpdateService;
        this.memberSignupService = memberSignupService;
        this.memberDeleteService = memberDeleteService;
    }

    public List<MemberResponse> getAllMember() {
        return memberBasicService.findAll();
    }

    public MemberResponse getMemberById(Long memberId) {
        return memberBasicService.getMemberById(memberId);
    }

    public boolean emailCheck(String email) {
        return memberBasicService.getMemberByEmail(email);
    }

    public void modifyMember(Long memberId, ModifyMemberRequest modifyMemberRequest) {
        memberBasicService.modifyMember(memberId, modifyMemberRequest);
    }

    public void modifyPassword(Long memberId, String password) {
        memberPasswordUpdateService.updatePassword(memberId, password);
    }

    public void updateDeleteAt(Long memberId) {
        memberDeleteService.updateDeleteAt(memberId);
    }

    public Long signup(CreateMemberRequest createMemberDTO) {
        return memberSignupService.signUp(createMemberDTO);
    }
}