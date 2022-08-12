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
 * RCS 템플릿 상세 조회
 */
public class TmplDetailAPI {

    public static void main(String[] args) {
        //브랜드 내에 등록된 템플릿 상세 조회합니다.

        ObjectMapper mapper = new ObjectMapper();

        //Header
        String apiId ="apiId";
        String apiSecret ="apiSecret";
        String brandKey = "brandKey";

        //Path Parameter
        String brandId = "brandId";
        String messagebaseId = "messagebaseId";


        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("https://api.msghub.uplus.co.kr/rcs/v1/brand/"+brandId+"/messagebase/"+messagebaseId);
            httpGet.setHeader("Content-Type","application/json");

            httpGet.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰
            httpGet.setHeader("apiId", apiId);
            httpGet.setHeader("apiSecret", apiSecret);
            httpGet.setHeader("brandKey", brandKey);

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

            String productCode;

            String spec;

            String cardType;

            String agencyId;

            String inputText;

            String attribute;

            String guideInfo;

            PolicyInfo policyInfo;
        }
    }

    @Data
    public static class PolicyInfo {

        String cardCount;

        String maxMediaSize;

        String maxTitleSize;

        String maxButtonCount;

        String maxDescriptionSize;

        String adBodyAllowed;

        String buttonsAllowed;

        String adHeaderAllowed;

    }
}
