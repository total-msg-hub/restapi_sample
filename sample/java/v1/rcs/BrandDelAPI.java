package v1.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * RCS 브랜드 삭제 요청
 */
public class BrandDelAPI {

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();

        //Header
        String apiId ="apiId";
        String apiSecret ="apiSecret";

        //Path Parameter
        String brandId = "brandId";


        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpDelete httpDel = new HttpDelete("https://api.msghub.uplus.co.kr/rcs/v1/brand/"+brandId);
            httpDel.setHeader("Content-Type","application/json");
            httpDel.setHeader("Authorization", "Bearer " + "YOUR_TOKEN"); // 인증 토큰
            httpDel.setHeader("apiId", apiId);
            httpDel.setHeader("apiSecret", apiSecret);

            // Response
            CloseableHttpResponse response = client.execute(httpDel);
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

            String brandId;
        }
    }
}
