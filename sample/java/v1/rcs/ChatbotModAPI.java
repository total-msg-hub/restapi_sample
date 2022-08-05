package v1.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;


public class ChatbotModAPI {

    public static void main(String[] args) {
        //챗봇 수정 요청

        ObjectMapper mapper = new ObjectMapper();
        RcsReq req = new RcsReq();

        ChatbotPostReg chatbot = new ChatbotPostReg();
        chatbot.setMdn("12341234");
        chatbot.setRcsReply("1");
        chatbot.setSubTitle("챗봇이름");
        chatbot.setService("01");
        req.setChatbot(chatbot);

        //Path Parameter
        String brandId = "brandId";
        String chatbotId = "chatbotId";


        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPut httpPut = new HttpPut("https://api.msghub.uplus.co.kr/rcs/v1/brand/"+brandId+"/chatbot/"+chatbotId);
            httpPut.setHeader("Content-Type","application/json");
            httpPut.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰

            StringEntity entity = new StringEntity(mapper.writeValueAsString(req),"UTF-8");
            httpPut.setEntity(entity);

            // Response
            CloseableHttpResponse response = client.execute(httpPut);
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

        //브랜드 등록,수정 정보
        ChatbotPostReg chatbot;
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
            List<ChatbotDetailAPI.Mediaurl> mediaUrl;

            //대표번호 여부
            boolean isMainNum;

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
