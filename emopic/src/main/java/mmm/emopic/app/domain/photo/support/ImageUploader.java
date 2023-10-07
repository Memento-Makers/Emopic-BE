package mmm.emopic.app.domain.photo.support;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Configuration
public class ImageUploader {

    @Value("${gcp.project.id}")
    private String projectId;
    @Value("${gcp.project.bucket.name}")
    private String bucketName;

    public String imageUpload(String objectName, MultipartFile image){
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        try {
            storage.create(blobInfo,image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(
                "File uploaded to bucket " + bucketName + " as " + objectName);
        return objectName;
    }
}
