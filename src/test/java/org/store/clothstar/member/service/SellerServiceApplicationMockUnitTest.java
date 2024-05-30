package org.store.clothstar.member.service;

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

@ExtendWith(MockitoExtension.class)
class SellerServiceApplicationMockUnitTest {
    @Mock
    SellerMybatisRepository sellerMybatisRepository;

    @InjectMocks
    SellerServiceApplication sellerServiceApplication;

    @DisplayName("판매회원 조회 단위 테스트")
    @Test
    void getSellerUnitTest() {
        //given
        Long memberId = 1L;
        Seller seller = mock(Seller.class);
        given(sellerMybatisRepository.findById(anyLong())).willReturn(Optional.of(seller));
        when(seller.getBizNo()).thenReturn("102-121-23323");

        //when
        //SellerResponse sellerResponse = sellerServiceApplication.getSellerById(memberId);

        //then
        verify(sellerMybatisRepository, times(1)).findById(anyLong());
        //assertThat(sellerResponse.getBizNo()).isEqualTo(seller.getBizNo());
    }
}