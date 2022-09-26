package v1.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * RCS 챗봇 리스트 조회
 */
public class ChatbotGetAPI {

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();

        //Header
        String apiId ="apiId";
        String apiSecret ="apiSecret";
        String brandKey ="brandKey";

        //Path Parameter
        String brandId ="brandId";
        String offset ="0";
        String limit ="100";


        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("https://api.msghub.uplus.co.kr/rcs/v1/brand/"+brandId+"/chatbot");
            httpGet.setHeader("Content-Type","application/json");

            httpGet.setHeader("Authorization", "Bearer " + "YOUR_TOKEN"); // 인증 토큰
            httpGet.setHeader("apiId", apiId);
            httpGet.setHeader("apiSecret", apiSecret);
            httpGet.setHeader("brandKey", brandKey);

            // Response
            CloseableHttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() > 299) {
                System.out.println("error. auth");
                return;
            }

            String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
            RcsRes res = mapper.readValue(jsonString, RcsRes.class);

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
     * 응답 dto
     */
    @Data
    public static class RcsRes {
        // 결과 코드
        String code;

        // 결과 코드 설명
        String message;

        // 결과 데이터
        List<Result> data;

        @Data
        public static class Result {

            //챗봇 ID
            String chatbotId;

            //브랜드 ID
            String brandId;

            //대표번호 여부를 표시
            String isMainNum;

            //발신번호, chatbotId와 동일
            String subNum;

            //챗봇(대화방) 이름
            String subTitle;

            //A2P/CHAT 서비스 유형
            String service;

            //챗봇 전시 설정값. 하단 참고
            String display;

            //승인 결과
            String approvalResult;

            //등록 일시
            String registerDate;

            //승인 일시
            String approvalDate;

            //수정 일시
            String updateDate;

            //등록 아이디
            String registerId;

            //수정 아이디
            String updateId;

        }
    }

    @Data
    public static class Mediaurl {

        String fileId;

        String fileName;

        String url;

        String typeName;

    }
}
