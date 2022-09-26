package v1.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * RCS 챗봇 등록 요청
 */
public class ChatbotRegAPI {

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();
        RcsReq req = new RcsReq();

        List<ChatbotPostReg> chatbots = new ArrayList<ChatbotPostReg>();
        ChatbotPostReg chatbot = new ChatbotPostReg();
        chatbot.setMdn("Mdn");
        chatbot.setRcsReply("1");
        chatbot.setSubTitle("SubTitle");
        chatbot.setService("01");
        chatbots.add(chatbot);
        req.setChatbots(chatbots);

        //Path Parameter
        String brandId = "brandId";


        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("https://api.msghub.uplus.co.kr/rcs/v1/brand/"+brandId+"/chatbot");
            httpPost.setHeader("Content-Type","application/json");
            httpPost.setHeader("Authorization", "Bearer " + "YOUR_TOKEN"); // 인증 토큰

            StringEntity entity = new StringEntity(mapper.writeValueAsString(req),"UTF-8");
            httpPost.setEntity(entity);

            // Response
            CloseableHttpResponse response = client.execute(httpPost);
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
     * 요청 dto
     */
    @Data
    public static class RcsReq {

        //업로드된 통신서비스이용증명원 파일경로
        String subNumCertificate;

        //챗봇 정보 목록
        List<ChatbotPostReg> chatbots;
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
        Result data;

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

            //대표번호 여부
            String isMainNum;

            //챗봇(대화방) 이름
            String subTitle;

            //A2P/CHAT 서비스 유형
            String service;

            //전시 설정값
            String display;

            //승인 상태
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

        }
    }

    @Data
    public static class ChatbotPostReg {

        //등록할 발신번호 *국가번호는 생략, 휴대전화 번호는 등록 불가
        String mdn;

        //0=SMS MO 수신, 1=RCS Postback 수신
        String rcsReply;

        //챗봇(대화방) 이름
        String subTitle;

        //A2P/CHAT 서비스 유형
        String service;

        //챗봇 전시 설정값. 하단 참고
        String display;

    }
}
