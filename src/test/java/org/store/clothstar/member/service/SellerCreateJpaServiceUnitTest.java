package org.store.clothstar.member.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.CreateSellerRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SellerCreateJpaServiceUnitTest {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    SellerCreateJpaServiceImpl sellerCreateJpaService;

    @Autowired
    private MemberSignupJpaServiceImpl memberSignupJpaService;

    private Long memberId;
    private Long memberId2;
    private String brandName = "나이키";
    private String bizNo = "102-13-13122";

    @DisplayName("회원가입과 판매자 신청을 진행 하고 memberId와 sellerId가 정상적으로 반환되는지 확인한다.")
    @BeforeEach
    public void signUp_getMemberId() {
        memberId = memberSignupJpaService.signUp(getCreateMemberRequest());
        memberId2 = memberSignupJpaService.signUp(getCreateMemberRequest2());
        entityManager.flush();
        entityManager.clear();

        Long sellerId = sellerCreateJpaService.sellerSave(memberId, getCreateSellerRequest());

        Assertions.assertThat(memberId).isNotEqualTo(null);
        Assertions.assertThat(sellerId).isNotEqualTo(null);

        //Seller의 키가 memberId이기 때문에 memberId와 sellerId는 같습니다.
        Assertions.assertThat(memberId).isEqualTo(sellerId);
    }

    @DisplayName("판매자 신청을 중복으로 하면 에러메시지를 응답한다.")
    @Test
    void sellerSaveDuplicateTest() {
        //given & when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            sellerCreateJpaService.sellerSave(memberId, getCreateSellerRequest());
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("이미 판매자 가입이 되어 있습니다.");
    }

    @DisplayName("같은 브랜드명으로 판매자 신청하면 에러 메시지를 응답한다.")
    @Test
    void brandnameDuplicateTest() {
        //given & when
        //브랜드명만 중복
        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, "102-13-13123"
        );

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            sellerCreateJpaService.sellerSave(memberId2, createSellerRequest);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 브랜드 이름 입니다.");
    }

    @DisplayName("같은 사업자번호로 판매자 신청하면 에러 메시지를 응답한다.")
    @Test
    void bizNoDuplicateTest() {
        //given & when
        //브랜드명만 중복
        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                "아디다스", bizNo
        );

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            sellerCreateJpaService.sellerSave(memberId2, createSellerRequest);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 사업자 번호 입니다.");
    }

    private CreateMemberRequest getCreateMemberRequest() {
        String email = "test@test.com";
        String password = "testl122";
        String name = "현수";
        String telNo = "010-1234-1245";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                email, password, name, telNo
        );

        return createMemberRequest;
    }

    private CreateMemberRequest getCreateMemberRequest2() {
        String email = "test2@test.com";
        String password = "testl122";
        String name = "현수";
        String telNo = "010-1234-1245";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                email, password, name, telNo
        );

        return createMemberRequest;
    }

    private CreateSellerRequest getCreateSellerRequest() {
        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, bizNo
        );

        return createSellerRequest;
    }
}