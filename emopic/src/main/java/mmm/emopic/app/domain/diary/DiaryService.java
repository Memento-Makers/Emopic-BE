package mmm.emopic.app.domain.diary;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.diary.dto.response.DiaryResponse;
import mmm.emopic.app.domain.diary.repository.DiaryRepository;
import mmm.emopic.app.domain.photo.Photo;
import mmm.emopic.app.domain.photo.repository.PhotoRepository;
import mmm.emopic.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final PhotoRepository photoRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public DiaryResponse saveDiary(String content, Long photoId){
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResourceNotFoundException("photo", photoId));
        Optional<Diary> optionalDiary = diaryRepository.findByPhotoId(photoId);
        Diary diary;
        Diary saveDiary;
        if(optionalDiary.isEmpty()){
            diary = Diary.builder().photo(photo).content(content).build();
            saveDiary = diaryRepository.save(diary);
        }
        else{
            saveDiary = optionalDiary.get();
            saveDiary.setContent(content);
        }

        return new DiaryResponse(saveDiary);
    }

}
