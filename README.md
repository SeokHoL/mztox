## 1. 🌤️ MZtoX
  
  MZ언어를 표준어로 바꿔 X세대도 알아들을수있는 번역기

>
>

  # 📃 프로젝트 정보

## 1. 제작기간
 2024.07.01 ~ 08.22

## 2. 참여 인원
| Name | Position |
|------|----------|
| [이석호]| Back |
| [변서진]| Front |
| [유승호]| AI |
| [오상현]| AI |

## 3. 역할 분담
- 팀원(백엔드담당)
- Springboot, SpringSecurity, SrpingJWT를 사용하여 로그인, 회원가입 및 번역 기능구현
>
## 🛠 사용 기술

### 1. Back-end
- Java
- Springboot
- SpirngSecurity, SpirngJWT

### 2. Front-end
- Javascript
- vue.js

### 3. AI
- python3
- flask

## 📊 Structure&API명세서

<details>
  <summary>Structure</summary>
  <img src="https://github.com/user-attachments/assets/6b3b69a3-1885-4ec4-aea3-2f1983cf6980">
</details>

<details>
  <summary>API명세서</summary>
  <img src="https://github.com/user-attachments/assets/edf38446-d38d-46d0-ac47-c9dbb13bfeb8" >
</details>

## 🔑 핵심기능

### 1. MZ언어를 표준어로 번역
사용자가 MZ언어를 입력하면 표준어로 번역하고, 사용자email에 따라 DB에 저장합니다.    
[코드 보러가기](https://github.com/SeokHoL/mztox/blob/master/src/main/java/com/example/mztox/controller/TranslationController.java)

### 2. 로그인을 하면 JWT토큰을 발급(1시간동안유지)
사용자가 로그인을 하면 JWT토큰을 발급하여 보안을 강화하고 1시간동안 세션을 관리합니다.      
[코드 보러가기](https://github.com/SeokHoL/mztox/blob/master/src/main/java/com/example/mztox/provider/JwtAuthProvider.java#L42)

## 📕 시현 이미지

<details>
  <summary>시현 이미지</summary>
  <img src="https://github.com/user-attachments/assets/48cca259-8f3c-4d73-a8c4-514f362f5721" >
</details>

