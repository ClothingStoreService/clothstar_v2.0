package org.store.clothstar.member.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.member.domain.Seller;

import java.util.Optional;

@Mapper
public interface SellerMybatisRepository {
    int save(Seller seller);

    Optional<Seller> findById(Long memberId);
}