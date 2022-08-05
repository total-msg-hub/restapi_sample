package v1.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;


public class BrandRegAPI {

    public static void main(String[] args) {
        //브랜드 등록 요청

        ObjectMapper mapper = new ObjectMapper();
        RcsReq req = new RcsReq();

        BrandPostReg regBrand = new BrandPostReg();
        regBrand.setName("Name");
        regBrand.setDescription("Description");
        req.setRegBrand(regBrand);

        //Header
        String apiId ="apiId";
        String apiSecret ="apiSecret";

        try {

            // Request
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("https://api.msghub.uplus.co.kr/rcs/v1/brand/");
            httpPost.setHeader("Content-Type","application/json");

            httpPost.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰
            httpPost.setHeader("apiId", apiId);
            httpPost.setHeader("apiSecret", apiSecret);

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
        // 브랜드 등록,수정 정보
        BrandPostReg regBrand;

        // 브랜드 대표 발신번호
        String mainMdn;

        //업로드된 브랜드 프로필 이미지경로
        String profileImgFilePath;

        //업로드된 브랜드 백그라운드 이미지경로
        String bgImgFilePath;

        //업로드된 통신서비스이용증명원 파일경로
        String certiFilePath;

        //등록할 챗봇(발신번호) 정보(최대 1000개)
        List<ChatbotPostReg> chatbots;
    }

    @Data
    public static class BrandPostReg {
        //브랜드 이름
        String name;

        //브랜드 설명
        String description;

        //브랜드 전화번호
        String tel;

        //랜드 홈에 표시 가능한 버튼 정보
        List<Menus> menus;

        //카테고리ID
        String categoryId;

        //하위카테고리ID
        String subCategoryId;

        //검색용 키워드
        String categoryOpt;

        //우편번호
        String zipCode;

        //도로명주소
        String roadAddress;

        //상세주소
        String detailAddress;

        //이메일주소
        String email;

        //홈페이지 주소
        String webSiteUrl;
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

            String brandId;


        }
    }

    @Data
    public static class Mediaurl {

        String fileId;

        String fileName;

        String url;

        String typeName;

    }

    @Data
    public static class Menus {

        //챗봇 버튼타입
        String buttonType;

        //buttonType call 을 제외한 모든 버튼은 Web URL을 입력해야함 http:// 또는 https:// 로 시작
        String weblink;

        //APP Link의 패키지 Name, Action, URL 정보
        AppLink applink;

    }

    @Data
    public static class AppLink {

        //App 실행을 위한 package name
        String packageName;

        //App 실행을 위한 Action
        String action;

        //App 실행을 위한 URI
        String url;

    }

    @Data
    public static class ChatbotPostReg {

        //등록할 발신번호 *국가번호는 생략, 휴대전화 번호는 등록 불가
        String mdn;

        //0=SMS MO 수신, 1=RCS Postback 수신
        String rcsReply;

        //챗봇(대화방) 이름
        String subTitle;

        //A2P/CHAT 서비스 유형
        String service;

        //챗봇 전시 설정값. 하단 참고
        String display;

    }
}
