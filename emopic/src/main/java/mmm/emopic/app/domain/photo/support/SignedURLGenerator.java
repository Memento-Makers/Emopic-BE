package mmm.emopic.app.domain.photo.support;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Getter
@Configuration
public class SignedURLGenerator {
    @Value("${gcp.project.id}")
    private String projectId;
    @Value("${gcp.project.bucket.name}")
    private String buckedName;
    @Value("${gcp.project.bucket.key-path}")
    private String keyPath;

    public String generateV4GetObjectSignedUrl(String objectName) throws StorageException, IOException {
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
        System.out.println(projectId);
        System.out.println(buckedName);
        System.out.println(keyPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(buckedName,objectName)).build();

        URL url = storage.signUrl(blobInfo,15, TimeUnit.MINUTES,Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.signWith(ServiceAccountCredentials.fromStream(new FileInputStream(keyPath))));
        return url.toString();
    }
}
