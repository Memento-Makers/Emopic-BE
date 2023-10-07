package mmm.emopic.app.domain.photo.support;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.web.client.RestTemplate;


import java.util.*;

@Configuration
public class PhotoInferenceWithAI {

    @Value("${ai-url}")
    private String inferenceUrl;
    public Optional<CategoryInferenceResponse> getClassificationsByPhoto(String signedUrl) {
        List<String> result= new ArrayList<>();

        String requestUrl = inferenceUrl+"classification";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("pic_path", signedUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "text/plain;charset=UTF-8");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(parameters, headers);


        RestTemplate rt = new RestTemplate();

        String response = rt.postForObject(requestUrl, request, String.class);

        ObjectMapper mapper = new ObjectMapper();

        CategoryInferenceResponse categoryInferenceResponse = null;
        try {
            return Optional.of(mapper.readValue(response, CategoryInferenceResponse.class));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public Optional<CaptionInferenceResponse> getCaptionByPhoto(String signedUrl){

        String requestUrl = inferenceUrl + "captioning";;

        Map<String, String> parameters = new HashMap<>();
        parameters.put("url", signedUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "text/plain;charset=UTF-8");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(parameters, headers);


        RestTemplate rt = new RestTemplate();

        String response = rt.postForObject(requestUrl, request, String.class);

        ObjectMapper mapper = new ObjectMapper();

        try {
            return Optional.of(mapper.readValue(response, CaptionInferenceResponse.class));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

}
