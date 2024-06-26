# member 패키지 README

## 패키지 개요

이 패키지는 회원에 대한 기능을 구현하기 위한 패키지입니다. 회원 도메인에 대한 시큐리티 적용을 달성하기 위한 규칙과 기획을 포함합니다.

### 회원가입 설계안

1. 이메일, 비밀번호, 이름, 나이, 전화번호를 입력  
   1-1. 이메일 중복체크
   1-2. 비밀번호 정규식으로 규칙 확인(규칙 : 영문자(대,소문자), 숫자, 특수문자를 포함하여 최소 8자 이상 20자 이하)
2. 유저 이메일로 링크 전송    
   2-1.전송된 이메일의 링크안에 JWT 임시 아이디가 전송됨
3. 링크를 눌렀을때 기입한 이메일 인증 완료
    - JWT의 임시 아이디와 동일한지 확인
4. 회원가입 완료
5. 가입 후 회원등급, 포인트 부여
    - 신규회원의 등급은 [ 브론즈 ]로 자동 할당됨
    - 회원등급은 구매금액에 따라 조정될 수 있음
6. DB에 저장되는 비밀번호는 암호화
    - 암호화는 현재 표준에 부합해야함(bcrypt, Argon2)
7. 생성일자는 현재일자 시간으로 자동 DB에 저장

### 로그인 설계안

1. 아이디, 비밀번호 체크후 로그인
2. JWT 토큰의 사용자 아이디, 비밀번호 인증절차를 진행 후 로그인
    - 아이디가 존재하지 않을 경우, ‘존재하지 않는 아이디입니다’ 알림
    - 비밀번호가 맞지 않을 경우, ‘잘못된 비밀번호입니다’ 알림
    - access 2분, refresh 20분 발급
    - 일반 회원의 경우 관리자 페이지, 판매자 페이지 못들어가도록 access 관리
    - 판매자 페이지는 관리자 페이지 못들어가도록 access 관리
    - refresh 토큰 발급 필요시 다시 로그인 필요

### 비밀번호 찾기 설계안

1. 가입한 이메일과
   1-1. 이메일이 가입한 유저가 맞는지 유효성 체크
2. 이메일로 비밀번호 변경 링크 전송  
   2-1. JWT 토큰으로 사용자 이메일로 링크를 타고 왔는지 검증
3. 비밀번호 변경 URL로 이동
4. 비밀번호 변경 URL에서 새로운 비밀번호, 새 비밀번호 확인 입력
    - 입력한 2개 비밀번호 동일한지 확인
5. 비밀번호 변경 완료

### 회원정보 수정페이지 이동 설계

1. 비밀번호 입력 (회원 정보 수정 시 비밀번호를 재확인해야 한다.)
    - 비밀번호 유효성 체크
2. 회원정보 수정 페이지 이동

### 비밀번호 수정 설계안

1. [ 현재 비밀번호, 새 비밀번호, 새 비밀번호 확인 ] 입력  
   1-1. 현재 비밀번호 맞는지 확인  
   1-3. [ 새 비밀번호 / 새 비밀번호 확인 ]이 동일한지 확인  
   1-4. 새 비밀번호가 기존 비밀번호가 다른지 확인  
   1.5. 비밀번호 정규식으로 규칙 확인(규칙 : 영문자(대,소문자), 숫자, 특수문자를 포함하여 최소 8자 이상 20자 이하)
   1-6. 만약 기존의 비밀번호와 같다면 ‘기존의 비밀번호와 동일합니다’ 알림

### 이메일 수정 설계안

1. 수정할 이메일 입력  
   1-1 입력한 이메일이 기존과 다른 이메일인지 확인
2. 인증메일 전송 버튼 누르면 수정한 이메일로 인증메일 전송, 인증메일 재전송 버튼과 이메일 변경 버튼 생성
3. 인증메일로 전송된 링크 누르면 이메일 인증 완료
4. 이메일 변경 버튼 클릭시 이메일 수정 완료

### 회원 배송지 설계안

1. 받는사람, 우편번호, 연락처 입력
   1-1. 우편번호 API 호출
   1-2. 받는사람, 배송지, 연락처 필수 항목 체크
   1-3. 기본 배송지는 한 회원마다 한개만 가능
2. 배송 요청사항 입력
   [목록]
    - 문 앞
    - 직접 받고 부재 시 문 앞
    - 경비실
    - 택배함
    - 기타사항
3. 공동현관 출입번호 입력
    - 비밀번호 없이 출입 가능 선택하면 입력 안해도 됨

### 판매자 설계안

1. 브랜드 대표 카테고리, 브랜드명, 공식 홈페이지, 회사명, 사업자 번호, 사업장 주소, 담당자명, 휴대전화번호, 이메일을 입력
   1-1. 사업자 번호는 중복 돼서는 안된다.
2. 입점 관리자가 입점을 승인하면 로그인이 가능하다.
   2-1. 사업자로 로그인 후 판매/상품/배송 관리를 할 수 있다.

### API 디자인

- 회원 가입 : POST /v1/members
    - 프로세스
        1. 이메일, 비밀번호, 이름, 생년월일, 전화번호 데이터 받음
        2. 이에밀 중복체크, 비밀번호 정규식 규칙 확인을 진행
            - 비밀번호 정규식 규칙 : 영문자(대,소문자), 숫자, 특수문자를 포함하여 최소 8자 이상 20자 이하
        3. 기입한 이메일로 인증용 링크 전송
        4. 임시 아이디를 주고 전송한 JWT 토큰의 아이디 값이랑 값이 맞으면 회원 가입 완료

- 회원 이메일 중복 체크 : GET /v1/members/email/{email}
    - 설명 : 회원 가입전 입력한 이메일이 중복 됐는지 체크 하는 api
    - 프로세스
        1. 성공하면 success : true
        2. 중복 됐으면 success : false, message : 중복된 이메일 입니다.

- 회원 가입 이메일 링크 확인 : POST /v1/members/signup/{id}/email
    - 설명 : 이메일로 전송된 링크를 눌렀는지 확인
    - 프로세스
        1. 링크에 있는 JWT 토큰의 임시 아이디로 검증
        2. 회원등급 브론즈 등급 부여
        3. 포인트 0 초기화
        4. 비밀번호 암호화 해서 DB 인입
        5. 생성시간 현재일 시간으로 자동 DB 인입

- 회원 전체 조회 : GET /v1/members
    - 설명 : 회원 전체 리스트를 조회 한다.

- 회원 조회 : GET /v1/members/{id}
    - 설명 : 멤버 아이디, 이메일, 이름, 전화번호, 총구매금액, 등급을 조회 한다.

- 회원 로그인 : POST /v1/members/login
    - 프로세스
        1. 입력한 아이디와 비밀번호 검증
        2. JWT 토큰의 사용자 아이디, 비밀번호 인증절차를 진행 후 로그인
            - 아이디가 유효하지 않을땐 ‘존재하지 않는 아이디입니다’ error 메시지 응답
            - 비밀번호가 맞지 않을 경우, ‘잘못된 비밀번호입니다’ error 메시지 응답
            - access 2분, refresh 20분 발급
            - 일반 회원의 경우 관리자 페이지, 판매자 페이지 못들어가도록 access 관리
            - 판매자 페이지는 관리자 페이지 못들어가도록 access 관리
            - refresh 토큰 발급 필요시 다시 로그인 필요

- 회원 비밀번호 찾기 : POST /v1/members/{id}/email
    - 프로세스
        1. 입력한 이메일, 아이디 검증
        2. 이메일로 비밀번호 변경 URL 전송

- 회원 유효성 확인 : POST /v1/members/{id}/{key}
    - 설명 : 회원의 비밀번호등 여러 필드들에 대한 유효성 검증을 위한 API
    - 비밀번호 유효성 프로세스
        1. 입력한 비밀번호가 맞는지 확인
            - 비밀번호가 맞지 않을 경우, ‘잘못된 비밀번호입니다’ error 메시지 응답

- 회원 정보 수정 : PATCH /v1/members/{id}/{key}
    - 설명 : Path 파라미터, {key}를 통해서 어떤 필드를 수정할 것인지 구별이 가능하다.
    - 이메일 수정 프로세스
        1. key값이 email 인지 확인
        2. 인증메일 API로 전송된 링크 클릭 했는지 확인
        2. 링크 클릭했으면 이메일 수정
    - 비밀번호 변경 프로세스
        1. key값이 password 인지 확인
        2. 입력한 비밀번호 정규식 규칙 확인
        2. 정규식 규칙에 맞으면 비밀번호 수정
    - 회원 권한 수정
        1. key값이 role 인지 확인

- 회원 배송지 입력 : POST /v1/members/{id}/address
    - 설명 : Path 파라미터, 멤버 아이디 파라미터를 받아서 회원에 대한 배송지를 저장
    - 프로세스
        1. 받는사람, 우편번호, 기본주소, 상세주소, 전화번호, 배송요청, 기본 배송지 유무 데이터를
        2. 기본 배송지는 한 회원마다 한개만 가능
        3. DB에 회원에 대한 주소를 저장

- 회원 배송지 조회 : GET /v1/members/{id}/address
    - 설명 : 회원 한명에 대한 전체 주소를 가져온다.
    - 프로세스
        1. 멤버 아이디, 받는자 이름, 우편번호, 기본주소, 상세주소, 전화번호, 배송요청, 기본 배송지 유무 조회

- 회원 기본 배송지 조회 : GET /v1/members/{id}/address/default
    - 설명 : 회원 한명에 대한 기본 배송지를 가져온다.

- 판매자 정보 입력 : POST /v1/sellers/{id}
    - 설명 : 멤버 아이디에 대한 판매자 정보를 기입 한다.
    - 프로세스
        1. 브랜드명, 사업자명 받는다.
        2. 전체 판매금액 0원 초기화 한다.
        3. 생성시간 현재일 시간으로 자동 DB 인입

- 판매자 정보 조회 : GET /v1/sellers/{id}
    - 설명 : 멤버아이디로 판매자 정보 조회(멤버 아이디, 브랜드명, 사업자명, 총 판매금액, 생성시간)
  
