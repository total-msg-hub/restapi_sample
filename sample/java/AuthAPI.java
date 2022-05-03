import javax.validation.constraints.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

public class AuthAPI {

	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		AuthReq req = new AuthReq();
		req.setApiKey("API 키");
		req.setApiPwd("암호화된 비밀번호");

		String randomStr = "YOUR_RANDOM_STRING"; // 비밀번호 암호화에 사용되는 랜덤 문자열. pattern:^[a-zA-Z0-9-_]{0,20}$

		try {

			// Request
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/auth/v1/"+ randomStr);
			postReq.setHeader("Content-Type", "application/json");

			String entity = mapper.writeValueAsString(req);
			postReq.setEntity(new StringEntity(entity));

			// Response
			CloseableHttpResponse response = client.execute(postReq);
			String jsonString = EntityUtils.toString(response.getEntity());
			AuthRes res = mapper.readValue(jsonString, AuthRes.class);

			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println(res);
			} else {
				System.out.println(res);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/** 인증 요청 dto */
	@Data
	public static class AuthReq {
		// API 키
		@Pattern(regexp="^[a-zA-Z0-9-_.]{0,20}$")
		String apiKey;

		// 암호화된 비밀번호
		String apiPwd;
	}

	/** 인증 응답 dto */
	@Data
	public static class AuthRes {
		// 결과 코드
		String code;

		// 결과 코드 설명
		String message;

		// 결과 데이터
		Result data;

		@Data
		public class Result {
			// 토큰
			String token;

			// 리프레시 토큰
			String refreshToken;
		}
	}
}
