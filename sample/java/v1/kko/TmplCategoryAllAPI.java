package v1.kko;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.util.List;


/**
 * 카카오 템플릿 카테고리 코드 조회
 */
public class TmplCategoryAllAPI {

    public static void main(String[] args) {
        //6. 템플릿 카테고리 코드를 조회합니다.

        ObjectMapper mapper = new ObjectMapper();

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/kko/v1/senderkey/template/category/all");
            postReq.setHeader("Content-Type","application/json");
            postReq.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰

            // Response
            CloseableHttpResponse response = client.execute(postReq);
            if (response.getStatusLine().getStatusCode() > 299) {
                System.out.println("error. auth");
                return;
            }

            String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
            KkoRes res = mapper.readValue(jsonString, KkoRes.class);

            if (response.getStatusLine().getStatusCode() == 200) {
                if (res.getData().size() > 0){
                    for(int i=0; i<res.getData().size(); i++) {
                        System.out.println("Code : " + res.getData().get(i).getCode());
                        System.out.println("Name : " + res.getData().get(i).getName());
                        System.out.println("GroupName : " + res.getData().get(i).getGroupName());
                    }
                }
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
    public static class KkoRes {
        // 결과 코드
        String code;

        // 결과 코드 설명
        String message;

        // 결과 데이터
        List<Result> data;

        @Data
        public static class Result {
            // 카테고리 코드
            String code;

            // 카테고리 이름
            String name;

            // 카테고리 그룹 이름
            String groupName;

            // 포함예시, 카테고리 적용대상 템플릿 설명
            String iclusion;

            // 제외예시, 카테고리 제외대상 템플릿 설명
            String exclusion;
        }
    }
}
