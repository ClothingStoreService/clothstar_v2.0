package org.store.clothstar.member.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    List<T> findAll();

    Optional<T> findById(Long memberId);

    Optional<T> findByEmail(String email);

//    T update(T t);
//
//    T save(T t);
}
