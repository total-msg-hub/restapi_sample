import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

public class MsgRcsAPI {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        MsgReq req = new MsgReq();

        req.setApiKey("APIKEY");
        req.setCallback("01012341234");

        req.setMessagebaseId("MessagebaseId");
        req.setHeader("0");
        req.setFooter("080-123-1234");
        req.setCopyAllowed(true);
        req.setExpiryOption("1");
        req.setAgencyId("uplus");

        List<RecvInfo> recvInfoLst = new ArrayList<RecvInfo>();
        RecvInfo recvInfo = new RecvInfo();
        recvInfo.setCliKey("1");
        recvInfo.setPhone("01012341234");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("additionalProp1", "string");
        hashMap.put("description", "{{additionalProp1}} 내용");
        recvInfo.setMergeData(new HashMap<String, String>(hashMap));
        recvInfoLst.add(recvInfo);
        req.setRecvInfoLst(recvInfoLst);

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/msg/v1/rcs");
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

        //메시지베이스 ID
        String messagebaseId;

        //0=정보성 메시지, 1=광고성 메시지
        String header;

        //무료수신거부 번호 (*header의 값이 광고성일 때 footer 값을 포함하지 않고 발송하면 실패 처리)
        String footer;

        //사용자의 복사/공유 허용여부
        Boolean copyAllowed;

        //expire 옵션(1:24시간, 2:30초)
        @Pattern(regexp= "^1$|^2$")
        String expiryOption;

        //대행사 아이디
        String agencyId;

        //캠페인 ID
        @Pattern(regexp = "^[a-zA-Z0-9-_]{0,20}$")
        String campaignId;

        //부서 코드
        @Pattern(regexp = "^[a-zA-Z0-9-_]{0,20}$")
        String deptCode;

        //버튼리스트
        List<MsgAlimtalkAPI.LMKakaoButton> buttons = new ArrayList<>();

        //발송정보 array
        List<RecvInfo> recvInfoLst = new ArrayList<RecvInfo>();

        //falback 정보 array
        List<FbInfo> fbInfoLst = new ArrayList<FbInfo>();

        //웹 요청 아이디(웹에서 요청 시 사용)
        String webReqId;

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

        //가변데이터
        private HashMap<String, String> mergeData;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FbInfo {

        //채널
        @NotNull
        private String ch;

        //제목
        private String title;

        //메시지
        private String msg;

        //파일아이디
        private String fileId;
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
