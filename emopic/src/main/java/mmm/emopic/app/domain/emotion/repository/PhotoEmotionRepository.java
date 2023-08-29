package mmm.emopic.app.domain.emotion.repository;

import mmm.emopic.app.domain.emotion.PhotoEmotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoEmotionRepository extends JpaRepository<PhotoEmotion,Long> {
    List<PhotoEmotion> findByPhoto_id(Long photoId);
}
