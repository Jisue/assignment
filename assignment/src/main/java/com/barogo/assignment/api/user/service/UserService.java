package com.barogo.assignment.api.user.service;

import com.barogo.assignment.api.global.exception.CommonException;
import com.barogo.assignment.api.user.domain.entity.User;
import com.barogo.assignment.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(User user) {
        User findUser = findUserById(user.getUserId());
        if (ObjectUtils.isNotEmpty(findUser)) {
            throw new CommonException("이미 가입된 ID입니다.", HttpStatus.BAD_REQUEST);
        }
        return userRepository.save(user);
    }

    private User findUserById(String id) {
        return userRepository.findByUserId(id).orElse(null);
    }

}
