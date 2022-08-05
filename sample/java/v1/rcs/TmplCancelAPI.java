package v1.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


public class TmplCancelAPI {

    public static void main(String[] args) {
        //템플릿 승인 요청을 취소

        ObjectMapper mapper = new ObjectMapper();

        //Path Parameter
        String brandId = "brandId";
        String messagebaseId = "messagebaseId";


        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPut httpPut = new HttpPut("https://api.msghub.uplus.co.kr/rcs/v1/brand/"+brandId+"/messagebase/"+messagebaseId+"/cancel");
            httpPut.setHeader("Content-Type","application/json");
            httpPut.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰

            // Response
            CloseableHttpResponse response = client.execute(httpPut);
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
        Object data;
    }
}
