package com.example.multimodal_projcet.repository;

import com.example.multimodal_projcet.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void save(Member member);                                       // 1. 회원 저장
    List<Member> findAll();                                         // 2. 회원 정보 전체 보기
    Optional<Member> findByUsername(String username);               // 3. 회원 정보 검색
    Optional<Member> findByEmail(String email);                     // 4. 회원 정보 검색
    void deleteById(String id);                                     // 5. 회원 탈퇴
}
