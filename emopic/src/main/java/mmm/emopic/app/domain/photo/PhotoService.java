package mmm.emopic.app.domain.photo;


import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.photo.dto.response.PhotoCaptionResponse;
import mmm.emopic.app.domain.photo.dto.request.PhotoUploadRequest;
import mmm.emopic.app.domain.photo.dto.response.PhotoUploadResponse;
import mmm.emopic.app.domain.photo.support.SignedURLGenerator;
import mmm.emopic.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final SignedURLGenerator signedURLGenerator;

    @Transactional
    public PhotoUploadResponse createPhoto(PhotoUploadRequest photoUploadRequest) {
        Photo photo = photoUploadRequest.toEntity();
        Photo savedPhoto = photoRepository.save(photo);
        String signedUrl;
        try {
            signedUrl = signedURLGenerator.generateV4PutObjectSignedUrl(savedPhoto.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PhotoUploadResponse(savedPhoto.getId(),signedUrl);
    }

    @Transactional
    public PhotoCaptionResponse getPhotoCaption(Long photoId){
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResourceNotFoundException("photo", photoId));
        return new PhotoCaptionResponse(photo.getCaption());

    }
}
