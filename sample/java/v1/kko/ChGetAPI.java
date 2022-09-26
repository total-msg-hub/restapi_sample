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
 * 카카오 채널 조회
 */
public class ChGetAPI {

    public static void main(String[] args) {
        //4. 카카오 채널을 조회합니다.

        ObjectMapper mapper = new ObjectMapper();
        KkoReq req = new KkoReq();

        req.setSenderKey("SenderKey");

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/kko/v1/senderkey/channel/get");
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
                System.out.println(res);
            } else {
                System.out.println(res);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 요청 dto
     */
    @Data
    public static class KkoReq {

        // 발신 프로필 키
        String senderKey;
    }

    /**
     * 응답 dto
     */
    @Data
    public static class KkoRes {
        // 결과 코드
        String code;

        // 결과 코드 설명
        String message;

        // 결과 데이터
        Result data;

        @Data
        public static class Result {
            //카카오 채널 ID
            String kkoChId;

            //발신 프로필 키
            String senderKey;

            //상태(A:정상, D:삭제)
            String stat;

            //카카오 채널 상태
            String kkoChStat;

            //차단 여부
            String block;

            //휴면 여부
            String dormant;

            //카테고리 코드
            String categoryCode;
        }
    }
}
