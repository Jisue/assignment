package com.barogo.assignment.api.global.security.service;

import com.barogo.assignment.api.global.exception.CommonException;
import com.barogo.assignment.api.global.security.jwt.AuthorityRoleType;
import com.barogo.assignment.api.global.security.jwt.BarogoUser;
import com.barogo.assignment.api.user.domain.entity.User;
import com.barogo.assignment.api.user.domain.enums.UserType;
import com.barogo.assignment.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new CommonException("해당하는 사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
    }

    private UserDetails createUserDetails(User user) {
        return BarogoUser.builder()
                .userId(user.getUserId())
                .username(user.getName())
                .password(user.getPassword())
                .authorities(List.of(user.getType() == UserType.ORDER ? AuthorityRoleType.ORDER_USER : AuthorityRoleType.DELIVER_USER))
                .build();
    }
}