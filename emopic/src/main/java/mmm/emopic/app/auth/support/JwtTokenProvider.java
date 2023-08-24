package mmm.emopic.app.auth.support;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import mmm.emopic.app.exception.auth.TokenNotValidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private Key secretKey;
    private final long accessTokenExpireTime; // 엑세스 토큰 만료시간
    private final long refreshTokenExpireTime; // 리프레시 토큰 만료시간

    public JwtTokenProvider(@Value("${jwt.token.secret-key}") String secretKey,
                            @Value("${jwt.token.access.expire-time}") long accessTokenValidityInSeconds,
                            @Value("${jwt.token.refresh.expire-time}") long refreshTokenValidityInSeconds ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpireTime = accessTokenValidityInSeconds * 1000;
        this.refreshTokenExpireTime = refreshTokenValidityInSeconds * 1000;
    }


    public String createAccessToken(Long id){
        return createToken(id,accessTokenExpireTime);
    }

    public String createRefreshToken(Long id){
        return createToken(id,refreshTokenExpireTime);
    }
    public String createToken(Long id, long validityInMilliseconds) {
        final String payload = String.valueOf(id);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

    }

    public String getPayload(String token) {
        return parseClaims(token)
                .getBody()
                .getSubject();
    }

    private Jws<Claims> parseClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new TokenNotValidException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new TokenNotValidException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new TokenNotValidException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new TokenNotValidException("JWT 토큰이 잘못되었습니다.");
        }
    }
}
