package mmm.emopic.app.domain.emotion;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.emotion.dto.request.EmotionUploadRequest;
import mmm.emopic.app.domain.emotion.dto.response.EmotionUploadResponse;
import mmm.emopic.app.domain.photo.Photo;
import mmm.emopic.app.domain.photo.PhotoRepository;
import mmm.emopic.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmotionService {
    private final EmotionRepository emotionRepository;
    private final PhotoRepository photoRepository;
    private final PhotoEmotionRepository photoEmotionRepository;

    public List<Emotion> findEmotions(){
        return emotionRepository.findAll();
    }

    @Transactional
    public EmotionUploadResponse saveEmotionsInPhoto(EmotionUploadRequest emotionUploadRequest, Long photoId){
        List<Long> emotionIdList = new ArrayList<>();
        emotionIdList.add(emotionUploadRequest.getEmotionId());
        emotionIdList.addAll(emotionUploadRequest.getChildEmotions());

        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResourceNotFoundException("photo", photoId));

        List<Long> photoEmotionIds = new ArrayList<>();
        for(Long eid: emotionIdList) {
            Emotion emotion = emotionRepository.findById(eid).orElseThrow(() -> new ResourceNotFoundException("emotion", eid));
            PhotoEmotion photoEmotion = emotionUploadRequest.toEntity(photo,emotion);
            PhotoEmotion savedPhotoEmotion = photoEmotionRepository.save(photoEmotion);
            photoEmotionIds.add(photoEmotion.getId());
        }

        return new EmotionUploadResponse(photoEmotionIds);
    }
}
