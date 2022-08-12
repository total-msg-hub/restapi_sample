package v1.kko;

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
 * 카카오 템플릿 생성
 */
public class TmplCreateAPI {

    public static void main(String[] args) {
        //8 템플릿을 생성합니다.

        ObjectMapper mapper = new ObjectMapper();
        KkoReq req = new KkoReq();

        req.setSenderKey("SenderKey");
        req.setSenderType("S");
        req.setTemplateName("TemplateName");
        req.setTemplateContent("emplateContent");
        req.setCategoryCode("CategoryCode");
        req.setTemplateMessageType("BA");
        req.setTemplateExtra("");
        req.setTemplateAd("");
        req.setTemplateEmphasizeType("NONE");
        req.setTemplateTitle("");
        req.setTemplateSubtitle("");
        req.setSecurityFlag("false");

        List<LMKakaoButton> buttons = new ArrayList<>();
        LMKakaoButton kakaoButton = new LMKakaoButton();
        kakaoButton.setName("연결");
        kakaoButton.setLinkType("WL");
        kakaoButton.setLinkMo("https://");
        kakaoButton.setLinkPc("https://");
        buttons.add(kakaoButton);
        req.setButtons(buttons);

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/kko/v1/senderkey/template/create");
            postReq.setHeader("Content-Type","application/json");
            postReq.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰

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
                System.out.println("Code= " +res.getCode());
                System.out.println("TemplateKey= " + res.getData().getTemplateKey());
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

        //발신 프로필 키
        String senderKey;

        //발신 프로필 타입(S:카카오채널, G:그룹)
        String senderType;

        //템플릿명
        String templateName;

        //템플릿내용
        String templateContent;

        //템플릿 카테고리코드
        String categoryCode;

        //메시지 유형
        String templateMessageType;

        //부가정보
        String templateExtra;

        //광고성메시지
        String templateAd;

        //강조표기유형
        String templateEmphasizeType;

        //강조표기핵심 정보
        String templateTitle;

        //강조표기보조 문구
        String templateSubtitle;

        //보안템플릿, 보안 템플릿 여부
        String securityFlag;

        //템플릿버튼
        List<LMKakaoButton> buttons;

    }

    @Data
    public static class LMKakaoButton{

        //버튼이름
        String name;

        String linkType;

        //모바일링크
        String linkMo;

        //pc 링크
        String linkPc;

        //android 실행
        String linkAnd;

        //ios 실행
        String linkIos;
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
            // 템플릿 키
            String templateKey;
        }
    }
}
