package mmm.emopic.app.auth;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.auth.Dto.LoginRequest;
import mmm.emopic.app.auth.Dto.TokenResponse;
import mmm.emopic.app.auth.member.Member;
import mmm.emopic.app.auth.member.MemberRepository;
import mmm.emopic.app.auth.refreshToken.RefreshToken;
import mmm.emopic.app.auth.refreshToken.RefreshTokenRepository;
import mmm.emopic.app.auth.support.JwtTokenProvider;
import mmm.emopic.app.exception.auth.PasswordMismatchException;
import mmm.emopic.app.exception.auth.TokenNotValidException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        //멤버 존재하는지 확인
        Member findMember = memberRepository.findByUserId(loginRequest.getUserId()).orElseThrow(() ->
                new RuntimeException(loginRequest.getUserId()));
        //비밀번호 검증
        validatePassword(findMember,loginRequest.getPassword());

        // RefreshToken 있는지 확인 후 토큰 두개 반환
        RefreshToken savedToken = refreshTokenRepository.findByMemberId(findMember.getId())
                .orElse(new RefreshToken(issueRefreshToken(findMember),findMember.getId()));
        try {// 토큰 만료기간이 지났으면 재발행
            jwtTokenProvider.validateToken(savedToken.getRefreshToken());
        }catch (TokenNotValidException e){
            savedToken.update(issueRefreshToken(findMember));
        }

        refreshTokenRepository.save(savedToken);

        return new TokenResponse(issueAccessToken(findMember),savedToken.getRefreshToken());
    }
    private String issueAccessToken(final Member findMember) {
        return jwtTokenProvider.createAccessToken(findMember.getId());
    }
    private String issueRefreshToken(final Member findMember) {
        return jwtTokenProvider.createRefreshToken(findMember.getId());
    }
    private void validatePassword(final Member findMember, final String password) {
        if (!passwordEncoder.matches(password, findMember.getPassword())) {
            throw new PasswordMismatchException();
        }
    }
}
