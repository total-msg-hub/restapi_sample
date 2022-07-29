package v1.kko;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import v1.auth.AuthAPI;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryAllAPI {

    public static void main(String[] args) {
        //1. 카카오 카테고리 정보 리스트를 조회합니다

        ObjectMapper mapper = new ObjectMapper();

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/kko/v1/senderkey/category/all");
            postReq.setHeader("Content-Type","application/json");
            postReq.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰

            // Response
            CloseableHttpResponse response = client.execute(postReq);
            if (response.getStatusLine().getStatusCode() > 299) {
                System.out.println("error. auth");
                return;
            }

            String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
            KkoRes res = mapper.readValue(jsonString, KkoRes.class);

            if (response.getStatusLine().getStatusCode() == 200) {
                if (res.getData().size() > 0){
                    for(int i=0; i<res.getData().size(); i++){
                        System.out.println("Code : " + res.getData().get(i).getCode());
                        System.out.println("Name : " + res.getData().get(i).getName());
                    }
                }
            } else {
                System.out.println(res);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 인증 응답 dto
     */
    @Data
    public static class KkoRes {
        // 결과 코드
        String code;

        // 결과 코드 설명
        String message;

        // 결과 데이터
        List<Result> data;

        @Data
        public static class Result {

            // 메시지 발송 결과 코드
            String code;

            // 메시지 발송 결과 코드 설명
            String name;
        }
    }
}
