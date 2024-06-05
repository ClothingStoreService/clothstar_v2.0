package org.store.clothstar.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.repository.SellerMybatisRepository;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SellerServiceApplicationMockUnitTest {
    @Mock
    SellerMybatisRepository sellerMybatisRepository;

    @InjectMocks
    SellerBasicServiceImpl sellerBasicService;

    @DisplayName("판매회원 조회 테스트 이다.(Mybatis)")
    @Test
    void getSellerUnitTest() {
        //given
        Long memberId = 1L;
        Seller seller = mock(Seller.class);
        given(sellerMybatisRepository.findById(anyLong())).willReturn(Optional.of(seller));
        when(seller.getMemberId()).thenReturn(memberId);

        //when
        Seller sellerDomainObject = sellerBasicService.getSellerById(memberId);

        //then
        verify(sellerMybatisRepository, times(1)).findById(memberId);
        Assertions.assertThat(sellerDomainObject.getMemberId()).isEqualTo(memberId);
    }
}