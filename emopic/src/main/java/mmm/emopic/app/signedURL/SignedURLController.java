package mmm.emopic.app.signedURL;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
public class SignedURLController {
  private final SignedURLService signedURLService;

  @GetMapping()
  public ResponseEntity getSignedUrl() throws IOException {
    String tmp="hi";
    String projectId = "EmoPic";
    String bucketName = "emopic-dev-bucket";
    String objectName = "testImage.jpg";

    tmp = signedURLService.generateV4GetObjectSignedUrl(projectId,bucketName,objectName);
    return ResponseEntity.ok(tmp);
  }
}
