package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceApplication {
    private final MemberBasicService memberBasicService;
    private final MemberPasswordUpdateService memberPasswordUpdateService;
    private final MemberDeleteService memberDeleteService;

    public List<MemberResponse> getAllMember() {
        return memberBasicService.findAll();
    }

    public MemberResponse getMemberById(Long memberId) {
        return memberBasicService.findById(memberId);
    }

    public boolean emailCheck(String email) {
        return memberBasicService.findByEmail(email);
    }

    @Transactional
    public void modifyMember(Long memberId, ModifyMemberRequest modifyMemberRequest) {
        memberBasicService.update(memberId, modifyMemberRequest);
    }

    @Transactional
    public void modifyPassword(Long memberId, String password) {
        memberPasswordUpdateService.updatePassword(memberId, password);
    }

    @Transactional
    public void updateDeleteAt(Long memberId) {
        memberDeleteService.updateDeleteAt(memberId);
    }

    @Transactional
    public Long signup(CreateMemberRequest createMemberDTO) {
        return memberBasicService.save(createMemberDTO);
    }
}