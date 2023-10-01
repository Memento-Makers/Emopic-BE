package mmm.emopic.app.domain.photo.support;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.photo.Photo;
import mmm.emopic.app.domain.photo.repository.PhotoRepository;
import mmm.emopic.app.domain.photo.repository.PhotoRepositoryCustomImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SignedURLScheduler {
    @Value("${gcp.project.bucket.ttl}")
    private long duration;
    private final SignedURLGenerator signedURLGenerator;
    private final PhotoRepository photoRepository;
    private final PhotoRepositoryCustomImpl photoRepositoryCustom;

    //@Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시에 실행
    @Scheduled(cron = "${scheduler.cron}")
    public void reGenerateSignedUrl() throws IOException {
        List<Photo> expiredList = photoRepositoryCustom.findAllByExpiredTime();
        for(Photo photo:expiredList){
            photo.setSignedUrl(signedURLGenerator.generateV4GetObjectSignedUrl(photo.getName()));
            photo.setSignedUrlExpireTime(LocalDateTime.now().plusMinutes(duration));
            photo.setTbSignedUrl(signedURLGenerator.generateV4GetObjectSignedUrl("thumbnail/"+photo.getName()));
            photo.setTbSignedUrlExpireTime(LocalDateTime.now().plusMinutes(duration));
            photoRepository.save(photo);
        }
    }
}
