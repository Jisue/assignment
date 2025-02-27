package com.barogo.assignment.api.global.security.jwt;

import com.barogo.assignment.api.global.exception.CommonException;
import com.barogo.assignment.api.user.domain.entity.User;
import com.barogo.assignment.api.user.domain.enums.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

    private final String secret;
    private final String refresh;
    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;

    private Key accesskey;
    private Key refreshKey;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.refresh}") String refresh,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        this.secret = secret;
        this.refresh = refresh;
        this.accessTokenValidityTime = accessTokenValidityInSeconds * 1000L;
        this.refreshTokenValidityTime = refreshTokenValidityInSeconds * 1000L;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refresh);
        this.accesskey = Keys.hmacShaKeyFor(keyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public JwtToken createJwtToken(Authentication authentication) {
        AuthorityRoleType authorityRoleType = AuthorityRoleType.valueOfRole(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining()));
        long nowMilli = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        JwtToken.AccessTokenInfo accessTokenInfo = createAccessToken(authentication, authorityRoleType, nowMilli);
        JwtToken.RefreshTokenInfo refreshTokenInfo = createRefreshToken(authentication, nowMilli);

        return JwtToken.builder().accessTokenInfo(accessTokenInfo).refreshTokenInfo(refreshTokenInfo).authorityRoleType(authorityRoleType).build();
    }

    private JwtToken.AccessTokenInfo createAccessToken(Authentication authentication, AuthorityRoleType authorityRoleType, long nowMilli) {
        BarogoUser user = (BarogoUser) authentication.getPrincipal();
        Claims claims = Jwts.claims()
                .add("userId", user.getUserId())
                .add("userName", user.getUsername())
                .add("roleCode", authorityRoleType.getCode())
                .build();

        Date issuedAt = new Date(nowMilli);
        Date expiration = new Date(nowMilli + accessTokenValidityTime);

        //토큰 빌더
        String accessToken = Jwts.builder()
                .setClaims(claims) // 정보
                .setIssuedAt(issuedAt) // 토큰 발생 시간 정보
                .setExpiration(expiration) // 만료 시간 설정
                .signWith(accesskey, SignatureAlgorithm.HS512)
                .compact(); // 토큰 생성

        return JwtToken.AccessTokenInfo.builder().accessToken(accessToken).accessTokenExpire(expiration.toInstant()).accessTokenIssue(issuedAt.toInstant()).build();
    }

    private JwtToken.RefreshTokenInfo createRefreshToken(Authentication authentication, long nowMilli) {
        BarogoUser user = (BarogoUser) authentication.getPrincipal();
        Claims claims = Jwts.claims()
                .add("userId", user.getUserId()).build();

        Date issuedAt = new Date(nowMilli);
        Date expiration = new Date(nowMilli + refreshTokenValidityTime);

        //토큰 빌더
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt) // 토큰 발생 시간 정보
                .setExpiration(expiration) // 만료 시간 설정
                .signWith(refreshKey, SignatureAlgorithm.HS512)
                .compact(); // 토큰 생성

        return JwtToken.RefreshTokenInfo.builder().refreshToken(refreshToken)
                .refreshTokenIssue(issuedAt.toInstant())
                .refreshTokenExpire(expiration.toInstant()).build();
    }

    public Authentication getAuthentication(User user) {
        UserDetails userDetails = BarogoUser.builder()
                .userId(user.getUserId())
                .authorities(List.of(user.getType() == UserType.ORDER ? AuthorityRoleType.ORDER_USER : AuthorityRoleType.DELIVER_USER))
                .username(user.getName())
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Authentication getAuthentication(JwtToken jwtToken) {
        Claims claims = parseAccessTokenClaimsWithoutException(jwtToken.getAccessTokenInfo().getAccessToken());

        List<AuthorityRoleType> grantedAuthorities = Collections.singletonList(jwtToken.getAuthorityRoleType());

        String userId = claims.get("userId", String.class);
        String userName = claims.get("userName", String.class);

        UserDetails userDetails = BarogoUser.builder()
                .userId(userId)
                .authorities(grantedAuthorities)
                .username(userName)
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public JwtToken resolveAccessToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(accesskey).build().parseClaimsJws(token).getBody();
            AuthorityRoleType authorityRoleType = AuthorityRoleType.ofCode(claims.get("roleCode", Integer.class));
            JwtToken.AccessTokenInfo accessTokenInfo = JwtToken.AccessTokenInfo.builder().accessTokenExpire(claims.getExpiration().toInstant()).accessToken(token).build();
            return JwtToken.builder().accessTokenInfo(accessTokenInfo).authorityRoleType(authorityRoleType).build();
        } catch (ExpiredJwtException e) {
            log.error("resolveAccessToken ExpiredJwtException: {}", e.getMessage(), e);
            throw new CommonException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("resolveAccessToken Exception: {}", e.getMessage(), e);
            throw new CommonException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    public Claims parseAccessTokenClaimsWithoutException(String token) {
        try {
            return Jwts.parser().setSigningKey(accesskey).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            //만료되었을때 claim 정보 가져오기
            log.error("parseAccessTokenClaimsWithoutException: {}", e.getClaims(), e);
            return e.getClaims();
        } catch (Exception e) {
            throw new CommonException();
        }
    }

}
