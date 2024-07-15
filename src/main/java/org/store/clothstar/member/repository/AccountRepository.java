package org.store.clothstar.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.member.domain.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    //    @Query("select acc from account acc join fetch acc.authorizations")
    @EntityGraph(attributePaths = {"authorizations"})
    Optional<Account> findById(Long accountId);
}
