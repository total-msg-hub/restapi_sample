# 메시지허브용 REST API 사용 예제

- U+ 메시지허브는 Restful API / 웹콘솔 환경에서 SMS/LMS/MMS, 카카오 비즈톡(알림톡/친구톡), RCS, 모바일 PUSH 메시지를 발송할 수 있는 비즈니스 플랫폼입니다.
 
- 메시지허브 서비스에 대한 상세 내용은 https://doc.msghub.uplus.co.kr/guide 를 참조하기 바랍니다.

- 메시지허브 서비스를 사용하기 위해서는 사전에 https://msghub.uplus.co.kr 에 가입해야 됩니다.

- 주의사항
  - 보안정책 상, 교차 출처 리소스 공유(CORS)를 지원하지 않기 때문에 JSP/Ajax를 활용하여 메시지 발송은 지원되지 않습니다. 

- 본 REST API 사용을 위한 사전준비사항
  - Java : OpenJDK 15
