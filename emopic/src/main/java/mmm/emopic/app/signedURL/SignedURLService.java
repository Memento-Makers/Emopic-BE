package mmm.emopic.app.signedURL;

import com.google.auth.ServiceAccountSigner;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.spring.core.Credentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class SignedURLService {

  public String generateV4GetObjectSignedUrl(String projectId, String bucketName, String objectName)
      throws StorageException, IOException {
    // String projectId = "my-project-id";
    // String bucketName = "my-bucket";
    // String objectName = "my-object";
    String keyPath = "src/main/resources/key.json";
    //.setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream("/key.json"))
    //Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("credentials.json"));
    Storage storage = StorageOptions.newBuilder()
        .setProjectId(projectId)
        .build()
        .getService();
    // Define resource
    BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();

    URL url =
        storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature(),Storage.SignUrlOption.signWith(ServiceAccountCredentials.fromStream(new FileInputStream(keyPath)) ));

    System.out.println("Generated GET signed URL:");
    System.out.println(url);
    System.out.println("You can use this URL with any user agent, for example:");
    System.out.println("curl '" + url + "'");
    String result ="";
    result += "Generated GET signed URL:"+"\n";
    result += url +"\n";
    result += "You can use this URL with any user agent, for example:"+"\n";
    result += "curl '" + url + "'";
    return result;
  }
}
