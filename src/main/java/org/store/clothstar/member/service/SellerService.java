package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.MessageDTOBuilder;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.dto.response.SellerResponse;
import org.store.clothstar.member.repository.SellerRepository;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public SellerResponse getSellerById(Long memberId) {
        return sellerRepository.findById(memberId)
                .map(SellerResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));
    }

    public MessageDTO sellerSave(Long memberId, CreateSellerRequest createSellerRequest) {
        Seller seller = createSellerRequest.toSeller(memberId);

        int result = sellerRepository.save(seller);
        if (result == 0) {
            throw new IllegalArgumentException("판매자 가입이 되지 않았습니다.");
        }

        return MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                "memberId : " + seller.getMemberId() + " 가 판매자 가입이 정상적으로 되었습니다."
        );
    }
}