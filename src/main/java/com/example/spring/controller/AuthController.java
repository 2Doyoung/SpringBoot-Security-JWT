package com.example.spring.controller;

import com.example.spring.auth.JwtTokenProvider;
import com.example.spring.model.User;
import com.example.spring.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    // 회원가입 API
    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 사용자 객체 생성 및 저장
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("USER"); // 기본 역할을 'USER'로 설정
        userRepository.save(user);

        return "User registered successfully";
    }

    // 로그인 API
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        // 사용자 조회
        User user = userRepository.findByUsername(username);

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(username, user.getRole());
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); // JavaScript에서 접근 불가
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1시간

        response.addCookie(cookie);
        return "redirect:/";
    }

    @GetMapping("/check")
    public String check(@CookieValue(value = "token", required = false) String token) {
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsernameFromToken(token);
            return "안녕하세요, " + username + "님!";
        }
        return "유효하지 않은 인증입니다.";
    }
}
