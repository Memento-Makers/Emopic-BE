# Emopic-BE
Emopic-BE 저장소에는 Emopic 프로젝트에 사용에서 주요 데이터에 대한 CRUD 기능을 하는 코드와 클라우드에서 썸네일을 만드는 코드가 저장되어 있습니다. 
(현재 최신 코드는 stage branch에 존재합니다. 직접 실행하기 위해서는 해당 브랜치의 코드를 사용해주세요!)

## Table Of content

- [기술 스택](#기술-스택)
- [준비 하기](#준비하기)
- [실행 하기](#실행하기)
- [참고 내용](#참고내용)
- [기여 하기](#기여하기)

## 기술 스택

### 언어

![Java](https://img.shields.io/badge/java-11-%233178C6)


### 라이브러리

![querydsl](https://img.shields.io/badge/querydsl-5.0.0-blue)

### 프레임워크

![Springboot](https://img.shields.io/badge/springboot-2.7.14-%236DB33F)


## 준비하기 
- spring boot와 연결할 Database를 구성해주세요
- Google Cloud Platform의 이미지 저장소를 만들고 credential.json 파일을 준비해 주세요
- Google Cloud Function에 썸네일을 만드는 함수를 추가해주세요
  - 이벤트 트리거는 위에서 만든 이미지 저장소의 google.cloud.storage.object.v1.finalized 로 설정해주세요
  - 썸네일을 만드는 함수는 파이썬으로 작성되어 있습니다. [링크](CloudFunction)
- Caption 결과 번역을 위해 DeepL key 혹은 papago key를 준비해주세요
- GPS 정보로 시도군 정보를 받아오기 위해 KakaoMap API Key를 준비해주세요
- AI 서버와 연결을 위해 [Emopic-AI 저장소](https://github.com/Memento-Makers/Emopic-AI.git)를 참고해 AI 서빙 서버를 구축해주세요



## 실행하기

환경변수 설정

#### 1. 레포지토리 클론 
```shell 
git clone https://github.com/Memento-Makers/Emopic-AI.git
```

#### 2. 환경변수 설정하기 

<details>

```
#Database
MYSQL_URL=<database_url>
MYSQL_USERNAME=<database_username>
MYSQL_PASSWORD=<database_password>

JWT_SECRET_KEY=<secret key>
JWT_ACCESS_TOKEN_EXPIRE_TIME=<token expire time>
JWT_REFRESH_TOKEN_EXPIRE_TIME=<token expire time>

#GCP
DURATION=<signed_url life time (minutes)>
PROJECT_ID=<project_id>
BUCKET_NAME=<bucket_name>
KEY_PATH=credential.json (gcs access key)

#inference-server
INFERENCE_URL=<ai-serving url>

#LOG
LOG_PATH=<log_path>

#DeepL
DEEPL_AUTH_KEY=<>

#Swagger
REQUEST_URL=<your domain>
REQUEST_URL_DESCRIPTION=개발 서버

#CORS
CORS_LIST=http://localhost:*,http://your-domain.com

#PAPAGO
PAPAGO_AUTH_ID=<>
PAPAGO_AUTH_KEY=<>


# 스케줄러 동작 -> 매일 새벽 3시에 예시
SCHEDULE=0 0 3 * * *

#File upload max size
MAX_FILE_SIZE = 20MB
MAX_REQUEST_SIZE = 20MB

#Kakao Map API # Kakao Map API Key
KAKAO_REST_API_KEY = <>
KAKAO_COORD_TO_REGION_URL = https://dapi.kakao.com/v2/local/geo/coord2regioncode

```
</details>
<div markdown="1">

#### 3. docker image 빌드하기
```shell 
docker build -t <imagename> . 
```

#### 4. docker image 실행시키기
```shell 
docker run --env-file .env -v credential.json:/key.json -p 8080:8080 -d <imagename> 
```


## 참고내용

### ERD
<img width="1557" alt="Screen Shot 2022-07-08 at 15 27 12" src="https://github.com/Memento-Makers/Emopic-BE/assets/80192612/cae8c1f4-443e-4f4f-9e3f-80584c4df8f0">

### API 명세
[Swagger API 명세](https://dev.emopic.shop/swagger-ui/index.html)

## [기여하기](docs/contribute.md)
