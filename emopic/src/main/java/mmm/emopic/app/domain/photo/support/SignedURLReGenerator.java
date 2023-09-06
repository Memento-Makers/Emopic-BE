package mmm.emopic.app.domain.photo.support;

import lombok.Getter;
import mmm.emopic.app.domain.photo.Photo;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

@Getter
@Configuration
public class SignedURLReGenerator {
    @Value("${DURATION}")
    private Long duration;

    public boolean ReGenerate(Photo photo){
        Optional<LocalDateTime> oldSignedUrlCreateTime = Optional.ofNullable(photo.getSignedUrlCreateTime());
        if(oldSignedUrlCreateTime.isEmpty()) return true;
        oldSignedUrlCreateTime.get().plusMinutes(duration);
        return LocalDateTime.now().isAfter(oldSignedUrlCreateTime.get());
    }
}
