package mmm.emopic.app.domain.photo.support;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Configuration
public class PhotoInferenceWithAI {

    @Value("${ai-url}")
    private String inferenceUrl;
    public List<String> getClassificationsByPhoto(String signedUrl) throws URISyntaxException, JsonProcessingException {
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

        CategoryInferenceResponse categoryInferenceResponse = mapper.readValue(response, CategoryInferenceResponse.class);
        result = categoryInferenceResponse.getCategories();

        return result;
    }

    public String getCaptionByPhoto(String signedUrl) throws URISyntaxException, JsonProcessingException {

        String requestUrl = inferenceUrl + "captioning";;

        Map<String, String> parameters = new HashMap<>();
        parameters.put("url", signedUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "text/plain;charset=UTF-8");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(parameters, headers);


        RestTemplate rt = new RestTemplate();

        String response = rt.postForObject(requestUrl, request, String.class);

        ObjectMapper mapper = new ObjectMapper();

        CaptionInferenceResponse captionInferenceResponse = mapper.readValue(response, CaptionInferenceResponse.class);
        String result = captionInferenceResponse.getCaption();
        return result;
    }

    public PhotoInferenceWithAI(){
    }
}
