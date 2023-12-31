package mmm.emopic.app.domain.photo.support;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import lombok.Getter;
import mmm.emopic.app.domain.photo.Photo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Getter
@Configuration
public class SignedURLGenerator {
    @Value("${gcp.project.id}")
    private String projectId;
    @Value("${gcp.project.bucket.name}")
    private String bucketName;
    @Value("${gcp.project.bucket.key-path}")
    private String keyPath;
    @Value("${gcp.project.bucket.ttl}")
    private Long duration;
    public String generateV4GetObjectSignedUrl(String objectName) throws StorageException, IOException {
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName,objectName)).build();

        URL url = storage.signUrl(blobInfo,duration, TimeUnit.MINUTES,Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.signWith(ServiceAccountCredentials.fromStream(new FileInputStream(keyPath))));
        return url.toString();
    }

    public String generateV4PutObjectSignedUrl(String objectName) throws StorageException, IOException {
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName,objectName)).build();

        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", "image/jpeg");

        URL url = storage.signUrl(
                blobInfo,
                duration,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withExtHeaders(extensionHeaders),
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.signWith(ServiceAccountCredentials.fromStream(new FileInputStream(keyPath))));

        return url.toString();
    }

    public boolean isExpired(Photo photo){
        Optional<LocalDateTime> oldSignedUrlExpireTime = Optional.ofNullable(photo.getSignedUrlExpireTime());
        if(oldSignedUrlExpireTime.isEmpty()) return true;
        return LocalDateTime.now().isAfter(photo.getSignedUrlExpireTime());
    }
}
