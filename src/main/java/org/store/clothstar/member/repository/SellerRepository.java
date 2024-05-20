package org.store.clothstar.member.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.member.domain.Seller;

import java.util.Optional;

@Mapper
public interface SellerRepository {
    public int save(Seller seller);

    public Optional<Seller> findById(Long memberId);
}
