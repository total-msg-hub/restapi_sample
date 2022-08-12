package v1.msg;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * 메시지 리포트 전달
 */
public class MsgRptAPI {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("https://api.msghub.uplus.co.kr/msg/v1/rpt");
            httpGet.setHeader("Content-Type","application/json");
            httpGet.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰

            // Response
            CloseableHttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() > 299) {
                System.out.println("error. auth");
                return;
            }

            String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
            MsgRes res = mapper.readValue(jsonString, MsgRes.class);
            if(res.getData() != null){
                for(int i=0; i<res.getData().getRptCnt(); i++){
                    RptLst rptList = res.getData().getRptLst().get(i);
                    System.out.println("Code : " + rptList.getResultCode());
                    System.out.println("MsgKey : " + rptList.getMsgKey());
                }
            }else if("10000".equals(res.getCode())){
                System.out.println("가져올 리포트가 존재하지 않습니다.");
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
     * 응답 dto
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

        //리포트 개수
        Integer rptCnt;

        List<RptLst> rptLst;

    }

    @Data
    public static class RptLst {

        //U+ 메시지 허브 시스템에서 부여한 메시지 고유 키
        String msgKey;

        //클라이언트키: 고객사에서 부여하는 메시지 고유 키
        String cliKey;

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
