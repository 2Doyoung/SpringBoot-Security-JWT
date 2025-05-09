package com.example.spring.auth;

// 로그인 완료가 되면 시큐리티 session 을 만들어줌 (Security ContextHolder)
// 오브젝트 : Authentication 타입 객체
// Authentication : User 정보
// User 타입 : UserDetails 타입 객체

import com.example.spring.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// Security Session : Authentication 타입 객체 : UserDetails 타입 객체
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 User 의 권한을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정이 만료되지 않았는지를 리턴합니다.
        // true를 리턴하면 계정이 만료되지 않았다는 뜻입니다.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호가 만료되지 않았는지를 리턴합니다.
        // true를 리턴하면 비밀번호가 만료되지 않았다는 뜻입니다.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정이 활성화되어 있는지를 리턴합니다.
        // true를 리턴하면 계정이 활성화된 상태입니다.
        // 예: 휴면 계정 처리 등에서 false로 설정할 수 있습니다.
        return true;
    }
}
