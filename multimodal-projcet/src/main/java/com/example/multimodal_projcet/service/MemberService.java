package com.example.multimodal_projcet.service;

import com.example.multimodal_projcet.domain.Member;
import com.example.multimodal_projcet.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerMember(Member member) {
        validateDuplicateUsername(member.getUsername());
        validateDuplicateEmail(member.getEmail());
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

        memberRepository.save(member);
        log.info("회원 가입 완료: {}", member.getUsername());
    }

    private void validateDuplicateUsername(String username) {
        memberRepository.findByUsername(username)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 등록된 아이디입니다.");
                });
    }

    private void validateDuplicateEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 등록된 이메일입니다.");
                });
    }

    @Transactional(readOnly = true)
    public List<Member> findAllMembers() {
        log.info("모든 회원 정보 조회");
        return memberRepository.findAll();
    }

    @Transactional
    public Member updateMember(String username, Member updatedMember) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        if (updatedMember.getPassword() != null) {
            member.setPassword(updatedMember.getPassword());
        }
        if (updatedMember.getEmail() != null) {
            validateDuplicateEmail(updatedMember.getEmail());
            member.setEmail(updatedMember.getEmail());
        }
        if (updatedMember.getStatus() != null) {
            member.setStatus(updatedMember.getStatus());
        }
        log.info("회원 정보 수정: {}", username);
        return member;
    }

    @Transactional
    public void deleteMember(String username) {
        memberRepository.deleteById(username);
        log.info("회원 삭제 완료: {}", username);
    }

    // **로그인 기능 추가**
    @Transactional(readOnly = true)
    public boolean login(String username, String password) {
        return memberRepository.findByUsername(username)
                .map(member -> passwordEncoder.matches(password, member.getPassword()))
                .orElse(false);
    }

    public void sendEmailVerification(String email) {
        log.info("이메일 인증 전송: {}", email);
        // 이메일 인증 로직
    }
}
