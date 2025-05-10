package com.example.spring.controller;

import com.example.spring.auth.JwtTokenProvider;
import com.example.spring.model.User;
import com.example.spring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/access")
    public String accessDenied() {
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
    public String login(@RequestParam String username, @RequestParam String password) {
        // 사용자 조회
        User user = userRepository.findByUsername(username);

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password ㅎ");
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(username, user.getRole());
        return "Bearer " + token;
    }
}
