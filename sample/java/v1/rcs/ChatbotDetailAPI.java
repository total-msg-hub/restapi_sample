package v1.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import v1.auth.AuthAPI;

import java.util.List;


public class ChatbotDetailAPI {

    public static void main(String[] args) {
        //챗봇 상세 조회

        ObjectMapper mapper = new ObjectMapper();

        //Header
        String apiId ="apiId";
        String apiSecret ="apiSecret";
        String brandKey ="brandKey";

        //Path Parameter
        String brandId ="brandId";
        String chatbotId ="chatbotId";


        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("https://api.msghub.uplus.co.kr/rcs/v1/brand/"+brandId+"/chatbot/"+chatbotId);
            httpGet.setHeader("Content-Type","application/json");

            httpGet.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰
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

            //그룹ID
            String groupId;

            //챗봇ID
            String chatbotId;

            //브랜드ID
            String brandId;

            //발신번호,chatbotId와 동일
            String subNum;

            //챗봇(대화방) 이름
            String subTitle;

            //A2P/CHAT 서비스 유형
            String service;

            //전시 설정값
            String display;

            //승인 결과
            String approvalResult;

            //챗봇 등록일시
            String registerDate;

            //챗봇 승인일시
            String approvalDate;

            //챗봇 수정일시
            String updateDate;

            //챗봇 등록 계정 ID
            String registerId;

            //챗봇 수정 계정 ID
            String updateId;

            //승인 사유
            String approvalReason;

            //챗봇 상태
            String status;

            //0=SMS MO 수신, 1=RCS Postback 수신
            String rcsReply;

            //검색 우선 순위, 기본값(512)
            String searchWeight;

            //webhook URLservice가 〈chat〉 인 경우 필수
            String webhook;

            //이용약관페이지 URL
            String botTcPage;

            //미디어 URL
            List<Mediaurl> mediaUrl;

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
