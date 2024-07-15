package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.common.error.ErrorCode;
import org.store.clothstar.common.error.exception.DuplicatedBizNoException;
import org.store.clothstar.common.error.exception.DuplicatedBrandNameException;
import org.store.clothstar.common.error.exception.DuplicatedSellerException;
import org.store.clothstar.member.domain.Account;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.repository.AccountRepository;
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
    private MemberRepository memberRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Member member;
    private Long memberId;
    private Long memberId2;
    private String brandName = "나이키";
    private String bizNo = "102-13-13122";

    @DisplayName("회원가입과 판매자 신청을 진행 하고 memberId와 sellerId가 정상적으로 반환되는지 확인한다.")
    @BeforeEach
    public void signUp_getMemberId() {
        String email1 = "test1@naver.com";
        String email2 = "test2@naver.com";

        Account account = accountRepository.save(CreateObject.getAccount(email1));
        member = memberRepository.save(CreateObject.getCreateMemberRequest(email1).toMember(account.getAccountId()));
        memberId = member.getMemberId();

        account = accountRepository.save(CreateObject.getAccount(email2));
        member = memberRepository.save(CreateObject.getCreateMemberRequest(email2).toMember(account.getAccountId()));
        memberId2 = member.getMemberId();

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