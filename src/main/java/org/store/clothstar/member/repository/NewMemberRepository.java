package org.store.clothstar.member.repository;

import org.store.clothstar.member.entity.MemberEntity;

public interface NewMemberRepository {
    void updateDeleteAt(MemberEntity memberEntity);

    void updatePassword(Long memberId, String password);
}
