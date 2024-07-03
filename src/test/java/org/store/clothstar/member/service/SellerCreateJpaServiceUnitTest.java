package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.util.CreateObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SellerCreateJpaServiceUnitTest {
    @Autowired
    SellerService sellerService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private MemberEntity memberEntity;
    private Long memberId;
    private Long memberId2;
    private String brandName = "나이키";
    private String bizNo = "102-13-13122";

    @DisplayName("회원가입과 판매자 신청을 진행 하고 memberId와 sellerId가 정상적으로 반환되는지 확인한다.")
    @BeforeEach
    public void signUp_getMemberId() {
        memberEntity = memberRepository.save(CreateObject.getCreateMemberRequest("test1@naver.com").toMemberEntity());
        memberId = memberEntity.getMemberId();

        memberEntity = memberRepository.save(CreateObject.getCreateMemberRequest("test2@naver.com").toMemberEntity());
        memberId2 = memberEntity.getMemberId();

        Long sellerId = sellerService.sellerSave(memberId, getCreateSellerRequest());

        assertThat(memberId).isNotEqualTo(null);
        assertThat(sellerId).isNotEqualTo(null);

        //Seller의 키가 memberId이기 때문에 memberId와 sellerId는 같습니다.
        assertThat(memberId).isEqualTo(sellerId);
    }

    @DisplayName("판매자 신청을 중복으로 하면 에러메시지를 응답한다.")
    @Test
    void sellerSaveDuplicateTest() {
        //given & when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            sellerService.sellerSave(memberId, getCreateSellerRequest());
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("이미 판매자 가입이 되어 있습니다.");
    }

    @DisplayName("같은 브랜드명으로 판매자 신청하면 에러 메시지를 응답한다.")
    @Test
    void brandNameDuplicateTest() {
        //given & when
        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, "102-13-13123"
        );

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            sellerService.sellerSave(memberId2, createSellerRequest);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 브랜드 이름 입니다.");
    }

    @DisplayName("같은 사업자번호로 판매자 신청하면 에러 메시지를 응답한다.")
    @Test
    void bizNoDuplicateTest() {
        //given & when
        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                "아디다스", bizNo
        );

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            sellerService.sellerSave(memberId2, createSellerRequest);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 사업자 번호 입니다.");
    }


    private CreateSellerRequest getCreateSellerRequest() {
        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, bizNo
        );

        return createSellerRequest;
    }
}