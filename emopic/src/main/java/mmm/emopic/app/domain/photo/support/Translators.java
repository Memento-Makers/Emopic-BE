package mmm.emopic.app.domain.photo.support;

import com.deepl.api.*;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

@Getter
@Configuration
public class Translators {

    @Value("${deepl-auth-key}")
    private String deeplAuthKey;

    private Translator translator ;

    @Value("${PAPAGO_AUTH_ID}")
    private String papagoAuthId;

    @Value("${PAPAGO_AUTH_KEY}")
    private String papagoAuthkey;

    public String deeplTranslate(String requiredTranslateText){
        translator = new Translator(deeplAuthKey);
        TextResult result = null;
        try {
            result = translator.translateText(requiredTranslateText,"EN" , "KO");
        } catch (DeepLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result.getText();
    }

    public String papagoTranslate(String requiredTranslateText){
        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";

        try {
            requiredTranslateText = URLEncoder.encode(requiredTranslateText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", papagoAuthId);
        requestHeaders.put("X-Naver-Client-Secret", papagoAuthkey);

        String responseBody = post(apiURL, requestHeaders, requiredTranslateText);

        return responseBody;
        //System.out.println(responseBody);
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text){
        HttpURLConnection con = connect(apiUrl);
        String postParams = "source=en&target=ko&text=" + text; //원본언어: 영어 (en) -> 목적언어: 한국어 (ko)
        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }
    private static String readBody(InputStream body) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(body));
        JSONObject jsonResult = (JSONObject) ((JSONObject) jsonObject.get("message")).get("result");
        return jsonResult.get("translatedText").toString();
    }
}
