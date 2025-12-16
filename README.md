# MobileApp
MobileApp
손대천

Login

- ComponentActivity : Compose UI용 액티비티
- setContent { } : XML 없이 화면 구성
- @Composable : UI 함수
- remember + mutableStateOf : 입력 상태 저장
- by 위임 : state.value 생략
- Column + Arrangement + Alignment : 세로 배치, 중앙 정렬
- OutlinedTextField : 텍스트 입력
- PasswordVisualTransformation : 비밀번호 마스킹
- Patterns.EMAIL_ADDRESS : 이메일 형식 검증
- when 조건문 : 입력값 분기 처리
- Toast : 결과 메시지 출력
- MaterialTheme / LoginTheme : 앱 테마 적용
- Modifier.fillMaxWidth / padding : 크기·여백 설정

 과정: 기본적으로 직접 코드를 짠 후 에러난 코드만 chat GPT에게 물어봄


memo

- ComponentActivity: Compose UI를 사용하는 액티비티 기본 클래스 (XML 미사용)

- @Composable: UI를 그리는 함수, 상태 변경 시 자동으로 화면 갱신

- remember + mutableStateOf: 상태(State) 저장, 값 변경 시 Recomposition 발생

- by 위임(Property Delegation): state.value 생략, 코드 간결화

- LaunchedEffect(Unit): 컴포지션 최초 1회 실행되는 사이드 이펙트 (초기 데이터 로드)

- SharedPreferences + edit {}: 간단한 로컬 데이터 저장, 람다로 자동 commit/apply

- 불변 리스트 업데이트 (memoList + text): 기존 데이터 유지 + 새 리스트 생성 → 상태 관리 안정적

- LazyColumn: 대량 리스트용 Compose UI (RecyclerView 대체)

- Modifier.clickable: View 없이 클릭 이벤트 처리

- LocalContext: Composable 내부에서 Context 접근

  과정: 수업시간 PPT 활용 및 인터넷 검색


<img width="402" height="872" alt="image" src="https://github.com/user-attachments/assets/4ceb30e1-33e4-453f-9429-40c7f0a81408" />

익명 타이머

- ComponentActivity : Compose UI 진입점
- setContent { } : Compose 화면 설정
- MaterialTheme : 머티리얼 디자인 적용
- @Composable : UI 구성 함수
- remember + mutableStateOf : 화면 상태 관리
- screen 상태값 : 입력 화면 / 타이머 화면 전환
- 조건 분기(if) : 화면 전환 처리
- 함수 분리(GoalInputScreen, TimerScreen) : 화면 역할 분리
- TextField 입력 필터 : 숫자만 입력 허용
- toIntOrNull : 안전한 숫자 변환
- 시간 계산(h*3600+m*60+s) : 초 단위 변환
- LaunchedEffect : 타이머 코루틴 실행
- delay(1000) : 1초 단위 카운트다운
- LazyColumn : 기록 목록 표시
- 불변 리스트(records + 항목) : 기록 추가
- Modifier : 레이아웃·스타일 설정
- Card / RoundedCornerShape : UI 카드 스타일링
- Color / FontWeight / sp : 색상·폰트 설정

 과정: 타이머 기능은 직접 만들고 익명 부분은 인터넷에서 찾아보다가 chat GPT에게 도움 받음

<img width="387" height="866" alt="image" src="https://github.com/user-attachments/assets/e995318c-e37d-402d-aa82-afd5d57eb5ab" />
<img width="386" height="856" alt="image" src="https://github.com/user-attachments/assets/cae9b512-b2c3-4002-aa37-d286d58c6cc0" />
<img width="391" height="868" alt="image" src="https://github.com/user-attachments/assets/fe3b8ca2-2723-4421-9f1b-3929ae5e53dc" />

