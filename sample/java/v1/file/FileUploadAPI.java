package v1.file;

import java.io.File;

import javax.validation.constraints.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

/**
 * 이미지 사전 등록
 */
public class FileUploadAPI {

	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		FileUploadReq req = new FileUploadReq();
		req.setFileId("YOUR_FILE_ID");		// 파일 아이디
		req.setWideYn("N");					// 와이드 이미지 여부(카카오 친구톡 파일 등록 시 사용)

		String ch = "YOUR_UPLOAD_CHANNEL";	// 채널(mms/rcs/friendtalk/push)

		try {

			// Request
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpPost postReq = new HttpPost("https://api.msghub.uplus.co.kr/file/v1/"+ ch);
			postReq.setHeader("Authorization", "YOUR_TOKEN"); // 인증 토큰

			HttpEntity entity = MultipartEntityBuilder.create()
					.addTextBody("reqFile", mapper.writeValueAsString(req))
					.addBinaryBody("filePart", new File("C:\\abc.jpg"), ContentType.MULTIPART_FORM_DATA, "abc.jpg").build();

			postReq.setEntity(entity);

			// Response
			CloseableHttpResponse response = client.execute(postReq);
			String jsonString = EntityUtils.toString(response.getEntity());
			FileUploadRes res = mapper.readValue(jsonString, FileUploadRes.class);

			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println(res);
			} else {
				System.out.println(res);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/** 파일 업로드 요청 dto */
	@Data
	public static class FileUploadReq {
		// 파일 아이디
		@Pattern(regexp="^[a-zA-Z0-9-_]{0,20}$")
		String fileId;

		// 와이드 이미지 여부(카카오 친구톡 파일 등록 시 사용)
		String wideYn;
	}

	/** 파일 업로드 응답 dto */
	@Data
	public static class FileUploadRes {
		// 결과 코드
		String code;

		// 결과 코드 설명
		String message;

		// 결과 데이터
		Result data;

		@Data
		public class Result {
			// 채널
			String ch;

			// 이미지 URL(mms,rcs인 경우 없음)
			String imgUrl;

			// 파일 ID
			String fileId;

			// 파일 만료일시
			String fileExpDt;
		}
	}
}
