package org.store.clothstar.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.MessageDTOBuilder;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(@Qualifier("memberJpaRepositoryAdapter") MemberRepository memberRepository
            , PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<MemberResponse> getAllMember() {
        List<MemberEntity> memberList = memberRepository.findAll();

        List<MemberResponse> memberResponseList = memberList.stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());

        return memberResponseList;
    }

    public MemberResponse getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));
    }

    public MessageDTO emailCheck(String email) {
        boolean emailExists = memberRepository.findByEmail(email).isPresent();

        return MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                (emailExists ? "이미 사용중인 이메일 입니다." : "사용 가능한 이메일 입니다."),
                emailExists
        );
    }


    public MessageDTO modifyMember(Long memberId, ModifyMemberRequest modifyMemberRequest) {
        Member member = modifyMemberRequest.toMember(memberId);
        memberRepository.update(member);

        return MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                "memberId : " + memberId + " 가 정상적으로 수정되었습니다.",
                true
        );
    }

    public MessageDTO signup(CreateMemberRequest createMemberDTO) {
        String encryptedPassword = passwordEncoder.encode(createMemberDTO.getPassword());
        Member member = createMemberDTO.toMember(encryptedPassword);

        int result = memberRepository.save(member);
        if (result == 0) {
            throw new IllegalArgumentException("회원 가입이 되지 않았습니다.");
        }

        return MessageDTOBuilder.buildMessage(
                member.getMemberId(),
                HttpStatus.OK.value(),
                "memberId : " + member.getMemberId() + " 가 정상적으로 회원가입 되었습니다."
        );
    }
}