package v1.kko;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * 카카오 인증 토큰 요청
 */
public class TokenAPI {

    public static void main(String[] args) {
        //2. 카카오 인증 토큰 요청하기

        ObjectMapper mapper = new ObjectMapper();
        KkoReq req = new KkoReq();

        req.setPhoneNumber("PhoneNumber");
        req.setKkoChId("KkoChId");

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/kko/v1/senderkey/token");
            postReq.setHeader("Content-Type","application/json");
            postReq.setHeader("Authorization", "Bearer " + "YOUR_TOKEN"); // 인증 토큰

            StringEntity entity = new StringEntity(mapper.writeValueAsString(req),"UTF-8");
            postReq.setEntity(entity);

            // Response
            CloseableHttpResponse response = client.execute(postReq);
            if (response.getStatusLine().getStatusCode() > 299) {
                System.out.println("error. auth");
                return;
            }

            String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
            KkoRes res = mapper.readValue(jsonString, KkoRes.class);

            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("Code : " + res.getCode());
                System.out.println("Message : " + res.getMessage());
            } else {
                System.out.println(res);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 메시지 dto
     */
    @Data
    public static class KkoReq {

        //휴대폰 번호
        String phoneNumber;

        //카카오 채널 ID
        String kkoChId;
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
    }
}
