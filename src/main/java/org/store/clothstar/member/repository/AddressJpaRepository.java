package org.store.clothstar.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.member.domain.Address;

public interface AddressJpaRepository extends JpaRepository<Address, Long> {

}
