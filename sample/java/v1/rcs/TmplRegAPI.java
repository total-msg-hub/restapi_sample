package v1.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;


public class TmplRegAPI {

    public static void main(String[] args) {
        //템플릿 등록 요청

        ObjectMapper mapper = new ObjectMapper();
        RcsReq req = new RcsReq();

        //Path Parameter
        String brandId = "brandId";

        req.setMessagebaseformId("string");
        req.setCustTmpltId("string");
        req.setTmpltName("string");
        req.setBrandId("string");

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("https://api.msghub.uplus.co.kr/rcs/v1/brand/"+brandId+"/messagebase");
            httpPost.setHeader("Content-Type","application/json");
            httpPost.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰


            StringEntity entity = new StringEntity(mapper.writeValueAsString(req),"UTF-8");
            httpPost.setEntity(entity);

            // Response
            CloseableHttpResponse response = client.execute(httpPost);
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
     * 요청 dto
     */
    @Data
    public static class RcsReq {

        //템플릿 양식 ID
        String messagebaseformId;

        //사용자 지정 템플릿ID
        String custTmpltId;

        //템플릿명
        String tmpltName;

        //브랜드 ID
        String brandId;

        //대행사 ID
        String agencyId;

        //오픈리치카드를 구성하는 Widget 의 속정 정보 객체
        Object formattedString;
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
        List<String> data;
    }
}
