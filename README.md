# 학교폭력 문제 해결을 위한 앱, “마음이”
2021 한이음 ICT 멘토링 프로젝트 [21_HF114] (2021.04 ~ )

<h2>개발 배경 및 필요성</h2>

- 학교폭력 피해 사례가 꾸준히 증가하는 추세를 보임
- 통계 조사 결과 2020년도 학교폭력 피해경험이 있는 학생은 2.7만명 이상으로 나타남
- 또한 학교폭력 가해 이유의 약 30%는 장난, 피해 미신고는 약 30%가 별일이 아니라 생각했기 때문이라고 응답한 것으로 나타남<br>
▶ 학생들의 `본인 행동에 대한 인지능력과 객관화의 미흡함`을 알 수 있음
- 따라서 `학교폭력이라는 사회적 문제를 해결`하기 위한 서비스를 개발하여 제공하고자 함

<h2>제공 기능</h2>
<h3>1) 학교폭력 진단테스트</h3>

- `학교폭력 피해, 가해 정도를 파악`할 수 있는 테스트
- 테스트 문항은 1388 청소년사이버상담센터와 유멘시아의 데이터를 조합하여 구성함
- 관련 학과 교수님의 센터 산하에 있는 상담가들의 의견하에 신뢰성을 검증받음

<h3>2) 학교폭력 AI 상담 채팅</h3>

- `Google Dialogflow 챗봇 API`를 활용한 학교폭력 상담 시스템으로, 사용자의 채팅에 `실시간으로 응답함`
- 앱 이용자에게 학교폭력 관련 법률적 조언, 상담 기관 안내 등의 정보를 제공하여 학교폭력 피해 학생을 지원함

<h3>3) 교화 프로그램 시청</h3>

- `YouTube API`를 활용한 학교폭력 관련 유튜브 영상 시청 기능

<h3>4) 일기장</h3>

- 일기 내용 작성 및 사진 첨부 가능
- `학교폭력 피해 증거 자료`로서 활용하는 목적으로 제공하는 기능

<h3>5) 게시판</h3>

- 앱 이용자의 `커뮤니케이션 공간` 제공
- 게시글 및 댓글 작성, 공감 기능으로 소통 가능

<h2>개발 환경</h2>

- IDE : Android Studio
- 개발 언어 : Java, XML
- 서버 및 DB : Google Firebase Authentication / Realtime Database / Storage
- API : Google DialogFlow, YouTube API
- 협업툴 : GitHub(GitLab), Figma, Google Drive


<h2>주요 적용 기술</h2>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/134390062-d2a2e572-7355-4514-9cc0-4184310e71f2.png">
</div>
<h3>1. Google Dialogflow</h3>

- NLP(자연어 처리)를 기반으로 구성된 챗봇
- 최종 사용자의 표현을 Agent(자연어 이해 모듈)에서 가장 유사한 Intent와 일치시키는 원리로 동작함
- 앱 내에서 학교폭력 `상담 기능` 구현 시 Dialogflow 챗봇 API를 사용함
- 앱 이용자의 학교폭력 예상 질문들과 그에 대한 답변 내용을 선별하여 Dialogflow의 Intent를 구성함
- 사용자가 앱에서 채팅을 보내면 Dialogflow 서비스에 해당 내용이 전송되고, Agent가 적절한 답변을 선택하여 앱으로 보내면 화면에 출력됨

<h3>2. Google Firebase - Authentication</h3>

- Firebase 인증 SDK에 사용자 인증 정보를 전달함
- 앱 내에서 `회원가입 및 로그인 기능` 제공 시 사용함

<h3>3. Google Firebase - Realtime Database</h3>

- NoSQL 클라우드 호스팅 데이터베이스로 실시간 데이터 동기화 기능을 제공함
- JSON 트리 구조로 데이터를 관리함
- 앱에서 사용되는 `데이터(일기, 게시글, 테스트 결과 등) 저장` 용도로 사용함

<h3>4. Google Firebase - Storage</h3>

- 선언적 보안 모델을 사용하여 사진, 동영상 등의 사용자 제작 콘텐츠를 저장하고 제공할 수 있음
- `일기장, 게시판` 글 등록 시 첨부하는 사진을 저장하는 용도로 사용함


<h2>앱 화면 캡쳐</h2>
<h3>메인 화면</h3>
<div align="center">

  <img src="https://user-images.githubusercontent.com/75527311/135826539-8c03e4c0-0e19-47af-b751-192e593bc2ab.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/135826572-d5aaf8a0-6bd0-4bf0-a9ae-27da423af555.jpg" width="300">
  <p>앱의 메인 화면 및 사이드바</p>
</div>

<h3>학교폭력 진단테스트</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/135826607-4d25b43d-027b-4169-ad23-9d21332b0a56.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/135826635-d4ba68c6-6d0b-499b-a0b0-952ad85f3481.jpg" width="300">
  <p>학교폭력 진단테스트 진행 및 결과 화면</p>
</div>

<h3>학교폭력 AI 상담 채팅</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/135826678-ce4b89d3-b056-4e60-8806-0d7ee8917a6e.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/135826707-be17e7c7-ce5f-44dd-b501-b46feec5cb77.jpg" width="300">
  <p>예상 질문을 제시함으로써 원활한 상담 진행을 유도하고, 진행 과정에서 학교폭력 상담 기관 안내 등의 정보를 제공함</p>
</div>

<h3>교화 프로그램 시청</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/134384795-e895fd0a-eb62-4196-8d31-00e16f74e9e5.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/134384821-d67db2b5-2cbd-4317-937e-70e399df57a3.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/134384858-4c7bc821-1ad2-4e85-93e1-4cce672fba48.jpg" width="300">
  <p>학교폭력 유튜브 영상 시청 시 마음 온도 증가</p>
</div>

<h3>나만의 일기장</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/135826747-87389a0c-f622-4a6c-bb40-dbe1f5a4252d.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/135827294-af9da3be-dbda-4d8e-9856-8d64ca5d4bf4.jpg" width="300">
  <p>일기 내용 작성 및 사진 첨부를 통해 학교폭력 피해 기록을 남길 수 있음</p>
</div>

<h3>게시판</h3>
<div align="center">
  <img src="https://user-images.githubusercontent.com/75527311/135826802-03c50e57-4818-40e2-b0bc-c60a28cce601.jpg" width="300">
  <img src="https://user-images.githubusercontent.com/75527311/135826836-46922584-2edd-4233-b879-972220f17943.jpg" width="300">
  <p>4개의 카테고리가 있는 게시판 기능</p>
</div>

<h2>개발 진행 프로세스</h2>
<h3>5월</h3>

- Android Studio - Github 연동 ✔
- Android Studio - Firebase 연동 ✔
- 주요 기능 및 메인 화면 레이아웃 구성 ✔

<h3>6월</h3>

- 일기장 주요 기능 구현 (일기 작성, 수정, 삭제) ✔
- 게시판 주요 기능 구현 (게시글 작성, 수정, 삭제) ✔

<h3>7월</h3>

- 사용자 계정 관리 ✔
  - 회원가입/로그인 기능 구현

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

- 사용자 테스트와 자체 피드백을 통한 앱 개선 방안 논의 ✔
- 지속적인 디버깅 및 오류 픽스, UI/UX 개선 ✔
- 2021 한이음 공모전 참가 - 현재 1차 서면평가 합격, 2차 준비 중 ✔
- ACK 2021 학술대회 참가 - 논문 작성 중 ✔
