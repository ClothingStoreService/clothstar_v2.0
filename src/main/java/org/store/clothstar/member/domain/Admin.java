package org.store.clothstar.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.store.clothstar.common.entity.BaseEntity;
import org.store.clothstar.member.dto.request.ModifyNameRequest;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "admin")
public class Admin extends BaseEntity {
    @Id
    private Long adminId;
    private String name;

    public void updateName(ModifyNameRequest modifyNameRequest, java.lang.reflect.Member member) {
        this.name = name;
    }
}
