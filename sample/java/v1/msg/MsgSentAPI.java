package v1.msg;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import v1.auth.AuthAPI;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

public class MsgSentAPI {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        MsgReq req = new MsgReq();

        req.setApiKey("APIKEY");

        List<String> msgKeyLst = new ArrayList<>();
        msgKeyLst.add("msgKey");
        req.setMsgKeyLst(msgKeyLst);

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/msg/v1/sent");
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
            if(res.getData() != null){
                for(int i=0; i<res.getData().getMsgKeyLst().size(); i++){
                    MsgKeyLst msgLst = res.getData().getMsgKeyLst().get(i);
                    System.out.println("MsgKey : " + msgLst.getMsgKey());
                    System.out.println("status : " + msgLst.getStatus());
                    System.out.println("ResultCode : " + msgLst.getResultCode());
                }
            }

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
     * 메시지 dto
     */
    @Data
    public static class MsgReq {

        // API 키
        @Pattern(regexp = "^[a-zA-Z0-9-_.]{0,20}$")
        String apiKey;

        //메시지키 목록
        List<String> msgKeyLst;
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
        Result data;

    }

    @Data
    public static class Result {

        List<MsgKeyLst> msgKeyLst;

    }

    @Data
    public static class MsgKeyLst {

        //U+ 메시지 허브 시스템에서 부여한 메시지 고유 키
        String msgKey;

        //클라이언트키: 고객사에서 부여하는 메시지 고유 키
        String cliKey;

        //메시지 상태
        String status;

        //채널
        String ch;

        //메시지 발송 결과코드
        String resultCode;

        //메시지 발송 결과코드 설명
        String resultCodeDesc;

        //Fallback 사유
        List<FbReason> fbReasonLst;

        //이통사
        String telco;

        //결과 수신 일시
        String rptDt;

    }

    @Data
    public static class FbReason {

        //채널
        String ch;

        //fallback 결과 코드
        String fbResultCode;

        //fallback 결과 코드 설명
        String fbResultDesc;

        //이통사
        String telco;
    }
}
