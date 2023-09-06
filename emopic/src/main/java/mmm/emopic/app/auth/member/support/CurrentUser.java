package mmm.emopic.app.auth.member.support;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Hidden //swagger에서 Member Entity가 보이지 않도록
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
// 익명 사용자인 경우에 null 로 설정.
// 인증된 사용자의 경우 member 로 설정해라!
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
public @interface CurrentUser {
}

