package v1.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * RCS 템플릿 수정 요청
 */
public class TmplModAPI {

    public static void main(String[] args) {
        //템플릿을 업데이트 합니다. 템플릿의 업데이트는 승인상태에 따라 처리 여부가 결정됩니다.

        ObjectMapper mapper = new ObjectMapper();
        RcsReq req = new RcsReq();

        //Path Parameter
        String brandId = "brandId";
        String messagebaseId = "messagebaseId";

        req.setMessagebaseformId("String");
        req.setCustTmpltId("String");
        req.setTmpltName("String");
        req.setBrandId(brandId);


        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPut httpPut = new HttpPut("https://api.msghub.uplus.co.kr/rcs/v1/brand/"+brandId+"/messagebase/"+messagebaseId);
            httpPut.setHeader("Content-Type","application/json");
            httpPut.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰

            StringEntity entity = new StringEntity(mapper.writeValueAsString(req),"UTF-8");
            httpPut.setEntity(entity);

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
        String data;
    }
}
