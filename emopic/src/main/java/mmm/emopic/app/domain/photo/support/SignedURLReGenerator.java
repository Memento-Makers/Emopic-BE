package mmm.emopic.app.domain.photo.support;

import lombok.Getter;
import mmm.emopic.app.domain.photo.Photo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Configuration
public class SignedURLReGenerator {
    @Value("${DURATION}")
    private Long duration;

    public boolean isExpired(Photo photo){
        Optional<LocalDateTime> oldSignedUrlCreateTime = Optional.ofNullable(photo.getSignedUrlExpireTime());
        if(oldSignedUrlCreateTime.isEmpty()) return true;
        return LocalDateTime.now().isAfter(photo.getSignedUrlExpireTime());
    }
}
