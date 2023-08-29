package mmm.emopic.app.auth.member.service;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.auth.member.Dto.MemberResponse;
import mmm.emopic.app.auth.member.Dto.RegisterRequest;
import mmm.emopic.app.auth.member.Member;
import mmm.emopic.app.auth.member.MemberRepository;
import mmm.emopic.app.base.Dto.SavedId;
import mmm.emopic.app.exception.member.DuplicateUserIdException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SavedId register(RegisterRequest registerRequest) {
        // 회원 아이디 중복 검사
        if(isDuplicateMemberId(registerRequest.getUserId())){
            throw new DuplicateUserIdException();
        }
        
        Member member = registerRequest.toEntity(passwordEncoder.encode(registerRequest.getPassword()));
        Member savedMember = memberRepository.save(member);
        return new SavedId(savedMember.getId());
    }

    private boolean isDuplicateMemberId(String memberId) {
        return memberRepository.existsByUserId(memberId);
    }

}
