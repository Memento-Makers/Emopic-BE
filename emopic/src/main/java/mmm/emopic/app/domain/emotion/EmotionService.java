package mmm.emopic.app.domain.emotion;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.emotion.dto.request.EmotionUploadRequest;
import mmm.emopic.app.domain.emotion.dto.response.EmotionMainSubResponse;
import mmm.emopic.app.domain.emotion.dto.response.EmotionResponse;
import mmm.emopic.app.domain.emotion.dto.response.EmotionRelatedPhotoResponse;
import mmm.emopic.app.domain.emotion.repository.EmotionRepository;
import mmm.emopic.app.domain.emotion.repository.PhotoEmotionRepository;
import mmm.emopic.app.domain.photo.Photo;
import mmm.emopic.app.domain.photo.repository.PhotoRepository;
import mmm.emopic.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmotionService {
    private final EmotionRepository emotionRepository;
    private final PhotoRepository photoRepository;
    private final PhotoEmotionRepository photoEmotionRepository;

    public EmotionMainSubResponse findEmotions(){
        List<EmotionResponse> eRList = emotionRepository.findAll().stream().map(EmotionResponse::new).collect(Collectors.toList());
        return new EmotionMainSubResponse(eRList);

    }

    @Transactional
    public EmotionRelatedPhotoResponse saveEmotionsInPhoto(EmotionUploadRequest emotionUploadRequest, Long photoId){
        List<Long> emotionIdList = new ArrayList<>();
        emotionIdList.add(emotionUploadRequest.getEmotionId());
        emotionIdList.addAll(emotionUploadRequest.getChildEmotions());

        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResourceNotFoundException("photo", photoId));
        List<PhotoEmotion> photoEmotionList = photoEmotionRepository.findByPhotoId(photo.getId());
        if(!photoEmotionList.isEmpty()){
            photoEmotionRepository.deleteAll(photoEmotionList);
        }
        List<Long> photoEmotionIds = new ArrayList<>();
        for(Long eid: emotionIdList) {
            Emotion emotion = emotionRepository.findById(eid).orElseThrow(() -> new ResourceNotFoundException("emotion", eid));
            PhotoEmotion photoEmotion = emotionUploadRequest.toEntity(photo,emotion);
            PhotoEmotion savedPhotoEmotion = photoEmotionRepository.save(photoEmotion);
            photoEmotionIds.add(photoEmotion.getId());
        }

        return new EmotionRelatedPhotoResponse(emotionIdList);
    }
}
