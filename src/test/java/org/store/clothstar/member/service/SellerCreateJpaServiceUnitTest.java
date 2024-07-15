package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.common.error.ErrorCode;
import org.store.clothstar.common.error.exception.DuplicatedBizNoException;
import org.store.clothstar.common.error.exception.DuplicatedBrandNameException;
import org.store.clothstar.common.error.exception.DuplicatedSellerException;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SellerCreateJpaServiceUnitTest {
    @Autowired
    SellerService sellerService;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Long memberId;
    private Long memberId2;
    private String brandName = "나이키";
    private String bizNo = "102-13-13122";

    @DisplayName("판매자 신청을 중복으로 하면 에러메시지를 응답한다.")
    @Test
    void sellerSaveDuplicateTest() {
        //given & when
        Throwable exception = assertThrows(DuplicatedSellerException.class, () -> {
            sellerService.sellerSave(memberId, getCreateSellerRequest());
        });

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.DUPLICATED_SELLER.getMessage());
    }

    @DisplayName("같은 브랜드명으로 판매자 신청하면 에러 메시지를 응답한다.")
    @Test
    void brandNameDuplicateTest() {
        //given & when
        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, "102-13-13123"
        );

        Throwable exception = assertThrows(DuplicatedBrandNameException.class, () -> {
            sellerService.sellerSave(memberId2, createSellerRequest);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.DUPLICATED_BRAND_NAME.getMessage());
    }

    @DisplayName("같은 사업자번호로 판매자 신청하면 에러 메시지를 응답한다.")
    @Test
    void bizNoDuplicateTest() {
        //given & when
        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                "아디다스", bizNo
        );

        Throwable exception = assertThrows(DuplicatedBizNoException.class, () -> {
            sellerService.sellerSave(memberId2, createSellerRequest);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.DUPLICATED_BIZNO.getMessage());
    }


    private CreateSellerRequest getCreateSellerRequest() {
        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, bizNo
        );

        return createSellerRequest;
    }
}