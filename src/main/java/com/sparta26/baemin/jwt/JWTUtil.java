package com.sparta26.baemin.jwt;


import com.sparta26.baemin.member.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
	// Header KEY 값
	public static final String AUTHORIZATION_HEADER = "Authorization";
	// 사용자 권한 값의 KEY
	// Token 식별자
	public static final String BEARER_PREFIX = "Bearer ";
	// 토큰 만료시간
	@Value("${jwt.time}")
	private long TOKEN_TIME;

	@Value("${jwt.key}") // Base64 Encode 한 SecretKey
	private String secretKey;
	private Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	// 로그 설정
	public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	// 토큰 생성
	public String createToken(Long id, String email, UserRole role) {
		Date date = new Date();

		return BEARER_PREFIX +
				Jwts.builder()
						.claim("id",id)
						.claim("email", email)
						.claim("role", role) // 사용자 권한
						.setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
						.setIssuedAt(date) // 발급일
						.signWith(key, signatureAlgorithm) // 암호화 알고리즘
						.compact();
	}

	// JWT Cookie 에 저장
	public void addJwtToCookie(String token, HttpServletResponse res) {
		try {
			token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

			Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
			cookie.setPath("/");

			// Response 객체에 Cookie 추가
			res.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * JWT 토큰 substring
	 */
	public String substringToken(String tokenValue) {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(7);
		}
		logger.error("Not Found Token");
		throw new NullPointerException("Not Found Token");
	}


	/**
	 * 토큰 검증
	 * @param token
	 * @return
	 */
	public void validateToken(String token) throws SecurityException, MalformedJwtException, SignatureException, ExpiredJwtException,UnsupportedJwtException, IllegalArgumentException
	{

		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

	}


	/**
	 * 토큰에서 사용자 정보 가져오기
	 * @param token
	 * @return
	 */
	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
	// HttpServletRequest 에서 Cookie Value : JWT 가져오기

	/**
	 * 요청에서 토큰 정보 빼기
	 * @param req
	 * @return
	 */
	public String getTokenFromRequest(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if(cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
					try {
						return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
					} catch (UnsupportedEncodingException e) {
						return null;
					}
				}
			}
		}
		return null;
	}
}
