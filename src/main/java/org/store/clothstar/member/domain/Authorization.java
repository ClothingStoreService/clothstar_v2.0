package org.store.clothstar.member.domain;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "authorizations")
public class Authorization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorizationId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public Authorization(Account account, MemberRole role) {
        this.account = account;
        this.role = role;
    }
}
