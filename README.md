# 마음이
2021 한이음 ICT 멘토링 프로젝트 [21_HF114] : 학교폭력 문제 해결을 위한 앱, <b>“마음이”</b>

<h2>주요 기능</h2>
<p>1) 학교폭력 진단테스트</p>
<p>2) 학교폭력 AI 상담 채팅 (챗봇)</p>
<p>3) 교화 프로그램 시청 (유튜브 영상 시청)</p>
<p>4) 일기장</p>
<p>5) 게시판</p>

<h2>개발 환경</h2>
<p>- <b>IDE</b> : Android Studio</p>
<p>- <b>개발 언어</b> : Java, XML</p>
<p>- <b>서버 및 DB</b> : Google Firebase (Authentication, Realtime Database, Storage)</p>
<p>- <b>API</b> : Google DialogFlow 챗봇 API, YouTube API</p>
<p>- <b>형상 관리</b>: GitHub, GitLab</p>

<h2>앱 화면 캡쳐</h2>
<h3>메인 화면</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/130937021-9f3a408f-b0ac-4e7e-b121-a78d457aad67.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/130937246-27e7a27d-1112-4304-8c47-8253058247c4.jpg" width="300">
  <p>메인 화면에 주요 기능들로 이동하는 버튼과 마음 온도 확인 가능, 사이드바 메뉴도 이용 가능</p>
</div>

<h3>학교폭력 진단테스트</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/130937904-b4c096ad-70a9-409a-9bed-2f6fabfb2caf.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/130937961-67f21a43-7989-495f-bc34-2290a1fc9758.jpg" width="300">
  <p>학교폭력 피해, 가해 정도를 스스로 파악할 수 있는 진단테스트</p>
</div>

<h3>학교폭력 AI 상담 채팅</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/130938574-3868d5af-0a4a-40b7-8e92-b26a193c112c.jpg" width="300">
  <p>학교폭력 관련 상담을 진행할 수 있는 채팅 기능, 챗봇 API를 활용하여 24시간 상담 가능</p>
</div>

<h3>교화 프로그램 시청</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/130939302-05675c3b-4ae3-422c-92ba-f0bfd32f76a8.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/130939618-8b146ed4-b40f-4e5b-985b-e9e89fc3a419.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/130939649-24478538-2aad-4d38-8d6b-9204f4d28141.jpg" width="300">
  <p>YouTube API를 이용하여 앱 내에서 유튜브 영상 시청 가능, 시청 완료 시 마음 온도 증가</p>
</div>

<h3>나만의 일기장</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/130942297-13cfb6b2-2c80-4f6b-954c-e845977e51f6.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/130942531-a791b2ba-9edf-4d78-8d5f-fa5028dc9d22.jpg" width="300">
  <p>사용자 본인만 조회할 수 있는 일기 작성 기능, 일기 내용 작성 및 사진 첨부 가능</p>
</div>

<h3>게시판</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/130942782-d162d46c-3562-4ad9-8f9d-bd9ce5c45c4b.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/130943002-79b7017e-fb8a-4fa5-9064-86855615a937.jpg" width="300">
  <p>앱 이용자들과 소통할 수 있는 커뮤니티 기능, 게시글 작성 / 댓글 작성 / 공감 기능</p>
</div>

<h2>개발 일정 관리</h2>
<h3>5월</h3>

- Android Studio - Github 연동 ✔
- Android Studio - Firebase 연동 ✔
- 주요 기능 및 메인 화면 레이아웃 구성 ✔

<h3>6월</h3>

- 일기장 주요 기능 구현 (일기 작성, 수정, 삭제) ✔
- 게시판 주요 기능 구현 (게시글 작성, 수정, 삭제) ✔

<h3>7월</h3>

- 사용자 계정 관리 ✔
  - 회원가입/로그인 기능 구현 (Firebase Authentication)

- 진단테스트 기능 구현 완료 ✔
  - 테스트 문항 구성 완료
  - 테스트 진행 및 결과 화면 구현

- 1:1 상담 기능 구현 완료 ✔
  - 1:1 상담 질문-응답 내용 구성 완료
  - Dialogflow 인텐트/엔티티 구성 완료
  - Android Studio - Dialogflow 연결

- 일기장 기능 보완 ✔
  - 이모티콘 추가
  - 사진 첨부 기능 추가

- 게시판 기능 추가 ✔
  - 댓글 작성/삭제 기능 구현
  - 게시글 공감 기능 추가

<h3>8월</h3>

- 진단테스트 결과에 따른 기능 제한 ✔
  - 가해 정도에 따라 마음 온도 값 설정
  - 가해 정도 '심함' 이상으로 나온 경우 게시판 이용 제한
  - 교화 프로그램 시청으로 마음 온도 60점 이상 달성 시 게시판 기능 해제

- 교화 프로그램 시청 ✔
  - Youtube API를 이용한 유튜브 영상 시청 기능
  - 각 영상을 끝까지 시청하면 마음 온도 증가

- 알림 기능 ✔
  - 회원가입 시, 새로운 댓글 등록 시, 마음 온도 60점 달성 시 알림 추가

- 앱 UI 보완 ✔
  - 메인 화면 디자인 수정
  - 일기장 이모티콘 이미지 수정

<h3>9월 이후 계획</h3>

- 사용자 테스트와 자체 피드백을 통한 앱 개선 방안 논의
- 지속적인 디버깅 및 오류 픽스, UI/UX 개선
- 2021 한이음 공모전 참가 (1차 서면평가 합격)
