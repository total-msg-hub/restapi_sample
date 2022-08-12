package v1.mo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;


/**
 * MO 메시지 조회
 */
public class MoMsgGetAPI {

    public static void main(String[] args) {
        //MO 메시지를 조회합니다.

        ObjectMapper mapper = new ObjectMapper();

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("https://api.msghub.uplus.co.kr/mo/v1/msg");
            httpGet.setHeader("Content-Type","application/json");
            httpGet.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰

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
        Result data;

        @Data
        public static class Result {

            //MO 건수
            String moCnt;

            //MO 리스트
            List<Mo> moLst;

        }
    }

    @Data
    public static class Mo {

        //MO 번호
        String moNumber;

        //MO 타입
        String moType;

        //	발신번호
        String moCallback;

        //상품코드
        String productCode;

        //MO 타이틀
        String moTitle;

        //메시지
        String moMsg;

        //이통사
        String telco;

        //수신시간
        String moRecvDt;

        //MMS MO 컨텐츠 개수
        String contentCnt;

        //MMS MO 컨텐츠 정보 리스트
        List<MoContent> contentInfoLst;

    }

    @Data
    public static class MoContent {

        //MMS MO 컨텐츠명
        String contentName;

        //MMS MO 컨텐츠 사이즈
        String contentSize;

        //MMS MO 컨텐츠 확장자
        String contentExt;

        //MMS MO 컨텐츠 URL
        String contentUrl;

    }
}
