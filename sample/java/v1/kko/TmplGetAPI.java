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
 * 카카오 템플릿 조회
 */
public class TmplGetAPI {

    public static void main(String[] args) {
        //10. 템플릿을 조회합니다.

        ObjectMapper mapper = new ObjectMapper();
        KkoReq req = new KkoReq();

        req.setSenderKey("SenderKey");
        req.setTemplateKey("TemplateKey");

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/kko/v1/senderkey/template/get");
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

        //템플릿 키
        String templateKey;
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
    }

    @Data
    public static class Result {
        //카카오 채널 ID
        String templateKey;

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
        boolean securityFlag;

        //템플릿버튼
        List<LMKakaoButton> buttons = new ArrayList<LMKakaoButton>();

        //템플릿 이미지 이름
        String templateImageName;

        //템플릿 이미지 URL
        String templateImageUrl;

        //템플릿코드
        String templateCode;

        //템플릿 상태 코드
        String templateStatusCode;

        //차단 여부
        String block;

        //휴면 여부
        String dormant;

        //생성 일자
        String createDate;

        //승인 일자
        String approvalDate;

        //문의 내용 목록
        List<comments> comments;
    }

    @Data
    public static class LMKakaoButton{

        //버튼이름
        String name;

        //종류
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

    @Data
    public static class comments{

        //문의 내용 번호
        String idx;

        //문의 내용 생성 ID
        String createId;

        //문의 내용
        String content;

        //문의 내용 생성 일자
        String createDate;

        //문의 내용 상태
        String status;
    }
}
