package com.barogo.assignment.api.user.domain.entity;

import com.barogo.assignment.api.user.domain.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id", length = 100, nullable = false, columnDefinition = "VARCHAR(100) COMMENT '고객 ID'")
    private String userId;

    @Column(name = "name", length = 100, nullable = false, columnDefinition = "VARCHAR(100) COMMENT '고객 이름'")
    private String name;

    @Column(name = "password", length = 100, nullable = false, columnDefinition = "VARCHAR(100) COMMENT '비밀번호'")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10, nullable = false, columnDefinition = "VARCHAR(10) COMMENT '고객 타입'")
    private UserType type;

    @Column(name = "create_date", nullable = false, columnDefinition = "TIMESTAMP(3) COMMENT '가입 날짜'")
    private LocalDateTime createDate;

    @Column(name = "update_date", columnDefinition = "TIMESTAMP(3) COMMENT '갱신 날짜'")
    private LocalDateTime updateDate;

}
