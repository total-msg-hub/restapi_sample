import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MsgFriendtalkAPI {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        MsgReq req = new MsgReq();

        req.setApiKey("APIKEY");

        req.setCallback("01012341234");
        req.setWideImageYn("N");
        req.setAdFlag("N");
        req.setMsg("친구톡 내용");
        req.setSenderKey("SenderKey");

        List<RecvInfo> recvInfoLst = new ArrayList<RecvInfo>();
        RecvInfo recvInfo = new RecvInfo();
        recvInfo.setCliKey("1");
        recvInfo.setPhone("01012341234");
        recvInfo.setCountryCd("82");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("string", "string");
        recvInfo.setMergeData(new HashMap<String, String>(hashMap));
        recvInfoLst.add(recvInfo);
        req.setRecvInfoLst(recvInfoLst);

        List<FbInfo> fbInfoLst = new ArrayList<FbInfo>();
        FbInfo fbInfo = new FbInfo();
        fbInfo.setCh("SMS");
        fbInfo.setTitle("제목");
        fbInfo.setMsg("SMS 대체발송");
        fbInfo.setFileId("string");
        fbInfoLst.add(fbInfo);
        req.setFbInfoLst(fbInfoLst);

        List<LMKakaoButton> buttons = new ArrayList<>();
        LMKakaoButton lMKakaoButton = new LMKakaoButton();
        lMKakaoButton.setName("웹 링크");
        lMKakaoButton.setLinkType("WL");
        lMKakaoButton.setLinkMo("https://");
        lMKakaoButton.setLinkPc("https://");
        buttons.add(lMKakaoButton);
        req.setButtons(buttons);

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/msg/v1/friendtalk");
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

            String jsonString = EntityUtils.toString(response.getEntity());
            MsgRes res = mapper.readValue(jsonString, MsgRes.class);

            if (response.getStatusLine().getStatusCode() == 200) {
                if (res.getData().size() > 0){
                    System.out.println("Code : " + res.getData().get(0).getCode());
                    System.out.println("Message : " + res.getData().get(0).getMessage());
                    System.out.println("MsgKey : " + res.getData().get(0).getMsgKey());
                }
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
    public static class MsgReq {

        // API 키
        @Pattern(regexp = "^[a-zA-Z0-9-_.]{0,20}$")
        String apiKey;

        //발신번호
        @Pattern(regexp = "^[0-9-]{0,20}$")
        String callback;

        //단축URL 사용여부
        @Pattern(regexp = "^[yYnN]{1,1}$")
        String clickUrlYn = "N";

        //필터링 여부
        String filterYn = "N";

        //필터그룹
        List<String> filterGrpLst = new ArrayList<>();


        //캠페인 ID
        @Pattern(regexp = "^[a-zA-Z0-9-_]{0,20}$")
        String campaignId;

        //부서 코드
        @Pattern(regexp = "^[a-zA-Z0-9-_]{0,20}$")
        String deptCode;

        //와이드 이미지 여부
        @Pattern(regexp= "^Y$|^N$")
        String wideImageYn;

        //파일아이디
        String fileId;

        //광고 표기 여부
        @Pattern(regexp= "^Y$|^N$")
        String adFlag;

        //메시지 내용
        String msg;

        //이미지
        Image image = new Image();

        //발신프로필키
        String senderKey;

        //버튼리스트
        List<LMKakaoButton> buttons = new ArrayList<>();

        //웹 요청 아이디(웹에서 요청 시 사용)
        String webReqId;

        //발송정보 array
        List<RecvInfo> recvInfoLst = new ArrayList<RecvInfo>();

        //falback 정보 array
        List<FbInfo> fbInfoLst = new ArrayList<FbInfo>();

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecvInfo {

        //클라이언트키
        @Pattern(regexp = "^[a-zA-Z0-9-_.@]{1,30}$")
        private String cliKey;

        //수신번호
        @Pattern(regexp = "^[0-9-]{1,20}$")
        private String phone;

        //국가 코드
        String countryCd;

        //가변데이터
        private HashMap<String, String> mergeData;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FbInfo {

        //채널
        private String ch;

        //제목
        private String title;

        //메시지
        private String msg;

        //파일아이디
        private String fileId;
    }

    @Data
    public static class LMKakaoButton{

        @Size(max = 14)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        //버튼이름
        String name;

        /*"버튼타입<br>"
        +"WL : linkMo 필수, linkPc 옵션<br>"
        +"AL : linkIos, linkAnd, linkMo 중 2가지 필수 입력, linkPc<br>"
        +"BK : 해당 버튼 텍스트 전송 <br>"
        +"MD : 해당 버튼 텍스트 + 메시지 본문 전송 <br>"
        +"BC : 상담톡 전환 <br>"
        +"BT : 봇 전환 <br>"
        +"DS : 메시지 내 송장번호 이용한 배송조회페이지로 연결 (quickReplies사용불가) <br>"
        +"AC : 채널추가 -광고추가형, 복합형템플릿에서만 사용가능 -버튼단톡 또는 최상단(첫번째버튼)에만 추가가능 (quickReplies사용불가)"
        */
        @Size(max = 2)
        String linkType;

        //mobile 환경에서 버튼 클릭 시 이동할 url
        String linkMo;

        //pc 환경에서 버튼 클릭 시 이동할 url
        String linkPc;

        //mobile android 환경에서 버튼 클릭 시 실행할 application custom scheme
        String linkAnd;

        //mobile ios 환경에서 버튼 클릭 시 실행할 application custom scheme
        String linkIos;
    }

    @Data
    public static class Image {

        //이미지 URL
        @Pattern(regexp= "^[a-zA-Z0-9-_:/]{1,300}$")
        private String imgUrl;

        //이미지 링크
        @Pattern(regexp= "^[a-zA-Z0-9-_:/]{1,300}$")
        private String imgLink;
    }

    /**
     * 인증 응답 dto
     */
    @Data
    public static class MsgRes {
        // 결과 코드
        String code;

        // 결과 코드 설명
        String message;

        // 결과 데이터
        List<Result> data;

        @Data
        public static class Result {

            // 클라이언트키: 고객사에서 부여하는 메시지 고유 키
            String cliKey;

            // U+ 메시지 허브 시스템에서 부여한 메시지 고유 키
            String msgKey;

            // 수신번호
            String phone;

            // 메시지 발송 결과 코드
            String code;

            // 메시지 발송 결과 코드 설명
            String message;

        }
    }
}
