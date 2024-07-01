package org.store.clothstar.member.service;

public interface MemberPasswordUpdateService {
    void updatePassword(Long memberId, String password);
}