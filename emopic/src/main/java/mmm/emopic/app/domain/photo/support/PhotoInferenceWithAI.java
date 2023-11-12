package mmm.emopic.app.domain.photo.support;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.*;

@Configuration
public class PhotoInferenceWithAI {

    @Value("${ai-url}")
    private String inferenceUrl;
    public void getClassificationsByPhoto(Long photoId, String signedUrl) {

        String requestUrl = inferenceUrl+"/classification";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("photo_id", String.valueOf(photoId));
        parameters.put("url", signedUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "text/plain;charset=UTF-8");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(parameters, headers);

        try{
            RestTemplate rt = new RestTemplate();
            rt.postForObject(requestUrl, request, String.class);
        }
        catch (HttpServerErrorException e){
            throw new RuntimeException("Classification 과정에서 오류 발생");
        }
    }

    public void getCaptionByPhoto(Long photoId, String signedUrl){

        String requestUrl = inferenceUrl + "/captioning";;

        Map<String, String> parameters = new HashMap<>();
        parameters.put("photo_id", String.valueOf(photoId));
        parameters.put("url", signedUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "text/plain;charset=UTF-8");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(parameters, headers);


        try {
            RestTemplate rt = new RestTemplate();
            rt.postForObject(requestUrl, request, String.class);
        }
        catch (HttpServerErrorException e){
            new RuntimeException("captioning 과정에서 오류 발생");
        }
    }

}
