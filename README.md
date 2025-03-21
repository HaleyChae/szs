1. Swagger UI : http://localhost:8080/3o3/swagger.html
2. H2 : http://localhost:8080/h2-console
3. 회원 가입 가능한 사용자 정보
   1. 사용자 정보는 /data/member.csv 파일로 설정합니다.
   2. 파일은 [이름,주민등록번호] 순으로 작성합니다.
4. 테스트 케이스
   1. /szs/signup 회원가입
      1. AuthControllerTest.signup: 정상 회원가입
      2. AuthControllerTest.signupNotValid: 필수 값 누락
      3. AuthControllerTest.signupInvalidPattern: 주민등록번호 형식 오류
      4. AuthControllerTest.signupUserNotEligible: 회원가입 불가 사용자
      5. AuthControllerTest.signupAlreadyRegistered: 이미 가입 된 회원
      6. AuthControllerTest.signupDuplicateUserId: 중복된 ID 가입
   2. /szs/login 로그인
      1. AuthControllerTest.login: 정상 로그인
      2. AuthControllerTest.loginNotValid: 필수 값 누락
      3. AuthControllerTest.loginInvalid: 로그인 실패
   3. /szs/scrap 스크랩
      1. ScrapControllerTest.scrap: 정상 스크랩
      2. ScrapControllerTest.scrapTokenExpire: 토큰 만료
      3. ScrapControllerTest.scrapNoToken: 비정상 토큰
      4. ScrapControllerTest.scrapApiResultFail : 토큰 API 조회 실패
   4. /szs/refund 결정세액 조회
      1. RefundControllerTest.refund: 정상 조회
      2. RefundControllerTest.refundTokenExpire: 토큰 만료
      3. RefundControllerTest.refundNoToken: 비정상 토큰
      4. RefundControllerTest.refundMissingDataForCalculation: 스크랩 정보 없음