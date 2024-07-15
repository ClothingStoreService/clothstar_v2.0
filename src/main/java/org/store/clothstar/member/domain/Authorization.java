package org.store.clothstar.member.domain;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "authorization")
public class Authorization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorizationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account userId;

    @Enumerated(EnumType.STRING)
    private MemberRole role;
}
