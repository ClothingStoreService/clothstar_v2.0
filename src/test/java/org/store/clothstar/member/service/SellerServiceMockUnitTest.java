package org.store.clothstar.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.response.SellerResponse;
import org.store.clothstar.member.repository.SellerRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceMockUnitTest {
    @Mock
    SellerRepository sellerRepository;

    @InjectMocks
    SellerService sellerService;

    @DisplayName("판매회원 조회 단위 테스트")
    @Test
    void getSellerUnitTest() {
        //given
        Long memberId = 1L;
        Seller seller = mock(Seller.class);
        given(sellerRepository.findById(anyLong())).willReturn(Optional.of(seller));
        when(seller.getBizNo()).thenReturn("102-121-23323");

        //when
        SellerResponse sellerResponse = sellerService.getSellerById(memberId);

        //then
        verify(sellerRepository, times(1)).findById(anyLong());
        assertThat(sellerResponse.getBizNo()).isEqualTo(seller.getBizNo());
    }
}