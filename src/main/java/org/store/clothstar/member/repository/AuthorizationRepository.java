package org.store.clothstar.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.member.domain.Authorization;

public interface AuthorizationRepository extends JpaRepository<Authorization, Long> {
}
