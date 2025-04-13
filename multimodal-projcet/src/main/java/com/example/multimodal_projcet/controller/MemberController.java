package com.example.multimodal_projcet.controller;

import com.example.multimodal_projcet.domain.Member;
import com.example.multimodal_projcet.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // 로그인 처리 (새로 추가)
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {
        boolean loginSuccess = memberService.login(username, password);
        if (loginSuccess) {
            return "redirect:/main_page.html";  // 로그인 성공 시 메인 페이지로 이동
        } else {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "login";  // 로그인 실패 시 다시 로그인 페이지로 이동
        }
    }

    // 회원가입 페이지 이동
    @GetMapping("/signup")
    public String showSignupPage() {
        return "register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String registerMember(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String email,
                                 Model model) {
        // 중복 확인 및 회원 등록
        try {
            Member member = new Member();
            member.setUsername(username);
            member.setPassword(password); // 실제로는 암호화 필요 (e.g., BCrypt)
            member.setEmail(email);
            memberService.registerMember(member);
            return "redirect:/members/login"; // 회원가입 후 로그인 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; // 에러 발생 시 회원가입 페이지로 돌아감
        }
    }

    // 회원 수정
    @PutMapping("/{username}")
    public ResponseEntity<String> updateMember(
            @PathVariable String username,
            @RequestBody Member updatedMember) {
        memberService.updateMember(username, updatedMember);
        return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
    }

    // 회원 삭제
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteMember(@PathVariable String username) {
        memberService.deleteMember(username);
        return ResponseEntity.ok("회원이 성공적으로 삭제되었습니다.");
    }

}
