package org.store.clothstar.member.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.entity.SellerEntity;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SellerJpaRepositoryAdapter implements SellerRepository {
    private final SellerJpaRepository sellerJpaRepository;

    @Override
    public Optional<Seller> findById(Long memberId) {
        SellerEntity sellerEntity = sellerJpaRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 판매자를 찾을 수 없습니다."));

        return Optional.of(new Seller(sellerEntity));
    }

    @Override
    public int save(Seller seller) {
        sellerJpaRepository.save(new SellerEntity(seller));
        return 1;
    }
}
