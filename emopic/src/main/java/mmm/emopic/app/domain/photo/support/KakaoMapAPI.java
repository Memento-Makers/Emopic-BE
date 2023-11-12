package mmm.emopic.app.domain.photo.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mmm.emopic.app.domain.location.Location;
import mmm.emopic.app.domain.photo.dto.response.KakaoCoord2regionResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;

@Configuration
public class KakaoMapAPI {
    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${kakao.api.geo.coord2regioncode}")
    private String rootUrl;

    public Optional<KakaoCoord2regionResponse> getLocationInfo(double latitude, double longitude){
         String requestUrl = String.format("%s.json?x=%s&y=%s",rootUrl,longitude,latitude);
        //System.out.println(requestUrl);

        StringBuffer response = new StringBuffer();


        //URL 설정
        try {
            URL url = new URL(requestUrl);

            //인증키 - KakaoAK하고 한 칸 띄워주셔야해요!
            String auth = "KakaoAK " + apiKey;

            //System.out.println(auth);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Request 형식 설정
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", auth);

            //request에 JSON data 준비
            conn.setDoOutput(true);

            //보내고 결과값 받기
            int responseCode = conn.getResponseCode();
            if (responseCode == 400) {
                System.out.println("400:: 해당 명령을 실행할 수 없음");
            } else if (responseCode == 401) {
                System.out.println("401:: Authorization가 잘못됨");
            } else if (responseCode == 500) {
                System.out.println("500:: 서버 에러, 문의 필요");
            } else { // 성공 후 응답 JSON 데이터받기

                Charset charset = Charset.forName("UTF-8");
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));

                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return getRegionAddress(response.toString());
    }

    /**
     * JSON형태의 String 데이터에서 주소값(address_name)만 받아오기
     */
    private static Optional<KakaoCoord2regionResponse> getRegionAddress(String jsonString) {
        JSONObject jObj = (JSONObject) JSONValue.parse(jsonString);
        JSONObject meta = (JSONObject) jObj.get("meta");
        long size = (long) meta.get("total_count");

        if(size>0){
            JSONArray jArray = (JSONArray) jObj.get("documents");
            try{
                ObjectMapper mapper = new ObjectMapper();
                for(Object j : jArray){
                    KakaoCoord2regionResponse result = mapper.readValue(j.toString(),KakaoCoord2regionResponse.class);
                    if(result.getRegion_type().equals("H")){
                        return Optional.of(result);
                    }
                }
            }
            catch (JsonProcessingException e){
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
