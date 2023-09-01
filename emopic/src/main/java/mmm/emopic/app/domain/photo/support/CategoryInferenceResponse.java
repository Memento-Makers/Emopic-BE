package mmm.emopic.app.domain.photo.support;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class CategoryInferenceResponse {
    private List<String> categories = new ArrayList<>();

    private String dateTime;

    public CategoryInferenceResponse(){

    }
}
