package mmm.emopic.app.domain.photo.support;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

@Configuration
public class ImageUploader {

    @Value("${gcp.project.id}")
    private String projectId;
    @Value("${gcp.project.bucket.name}")
    private String bucketName;

    @Value("${gcp.project.bucket.key-path}")
    private String keyPath;

    public String imageUpload(String objectName, MultipartFile image) {

        try{
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));
            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentials).build().getService();
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("image/jpeg").build();
            storage.create(blobInfo,image.getBytes());
            //System.out.println("File uploaded to bucket " + bucketName + " as " + objectName);
            return objectName;
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
