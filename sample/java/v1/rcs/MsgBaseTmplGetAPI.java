package v1.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * RCS 이통사 기본 템플릿 조회
 */
public class MsgBaseTmplGetAPI {

    public static void main(String[] args) {
        //이통사가 기본 제공하는 템플릿을 조회합니다.

        ObjectMapper mapper = new ObjectMapper();

        //Header
        String apiId ="apiId";
        String apiSecret ="apiSecret";

        //Path Parameter
        String offset ="0";
        String limit ="100";


        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("https://api.msghub.uplus.co.kr/rcs/v1/messagebase/common");
            httpGet.setHeader("Content-Type","application/json");

            httpGet.setHeader("Authorization", "Bearer " + "YOUR_TOKEN"); // 인증 토큰
            httpGet.setHeader("apiId", apiId);
            httpGet.setHeader("apiSecret", apiSecret);

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
        List<Result> data;

        @Data
        public static class Result {

            String groupId;

            String messagebaseId;

            String tmpltName;

            String messagebaseformId;

            String brandId;

            String status;

            String approvalResult;

            String approvalReason;

            String registerDate;

            String approvalDate;

            String updateDate;

            String registerId;

            String updateId;

        }
    }
}
