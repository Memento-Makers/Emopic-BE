package mmm.emopic.app.domain.emotion.repository;

import mmm.emopic.app.domain.emotion.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmotionRepository extends JpaRepository<Emotion,Long> {

}
