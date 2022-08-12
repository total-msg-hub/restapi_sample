package v1.msg;

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

/**
 * 메시지 통합 메시지 발송
 */
public class MsgSmartMsgAPI {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        MsgReq req = new MsgReq();

        req.setApiKey("APIKEY");
        req.setTmpltCode("TmpltKey");

        List<SmartRecvInfo> recvInfoLst = new ArrayList<SmartRecvInfo>();
        SmartRecvInfo recvInfo = new SmartRecvInfo();
        recvInfo.setCliKey("1");
        recvInfo.setPhone("01012341234");
        recvInfo.setCuid("Cuid");
        HashMap<String, String> hashMap1 = new HashMap<>();
        HashMap<String, HashMap<String, String>> hashMap = new HashMap<>();
        hashMap1.put("additionalProp1", "string");
        hashMap.put("SMS",hashMap1);
        recvInfo.setMergeData( new HashMap<String, HashMap<String, String>> (hashMap));
        recvInfoLst.add(recvInfo);
        req.setRecvInfoLst(recvInfoLst);

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/msg/v1/smartMsg");
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

        //단축URL 사용여부
        @Pattern(regexp = "^[yYnN]{1,1}$")
        String clickUrlYn = "N";

        //필터링 여부
        String filterYn = "N";

        //필터그룹
        List<String> filterGrpLst = new ArrayList<>();

        //통합발송 템플릿
        String tmpltCode;

        //캠페인 ID
        @Pattern(regexp = "^[a-zA-Z0-9-_]{0,20}$")
        String campaignId;

        //부서 코드
        @Pattern(regexp = "^[a-zA-Z0-9-_]{0,20}$")
        String deptCode;

        //웹 요청 아이디(웹에서 요청 시 사용)
        String webReqId;

        //발송정보 array
        List<SmartRecvInfo> recvInfoLst = new ArrayList<SmartRecvInfo>();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SmartRecvInfo {

        //클라이언트키
        @Pattern(regexp = "^[a-zA-Z0-9-_.@]{1,30}$")
        private String cliKey;

        //수신번호
        @Pattern(regexp = "^[0-9-]{1,20}$")
        private String phone;

        //앱 로그인 시 사용되는 아이디
        String cuid;

        //가변데이터
        private HashMap<String, HashMap<String, String>> mergeData;
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
