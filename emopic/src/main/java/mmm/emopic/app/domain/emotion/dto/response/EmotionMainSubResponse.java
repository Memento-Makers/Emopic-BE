package mmm.emopic.app.domain.emotion.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EmotionMainSubResponse {
    private final List<EmotionResponse> main = new ArrayList<>();
    private final List<EmotionResponse> sub = new ArrayList<>();

    public EmotionMainSubResponse(List<EmotionResponse> emotionResponseList){
        for(EmotionResponse eRList : emotionResponseList){
            Long eid = eRList.getEmotionId();
            if(eid>=1 && eid<=3){
                this.main.add(eRList);
            }
            else{
                this.sub.add(eRList);
            }
        }
    }
}
