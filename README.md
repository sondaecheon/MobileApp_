# MobileApp
MobileApp
손대천

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
- 

<img width="402" height="872" alt="image" src="https://github.com/user-attachments/assets/4ceb30e1-33e4-453f-9429-40c7f0a81408" />

익명 타이머


<img width="387" height="866" alt="image" src="https://github.com/user-attachments/assets/e995318c-e37d-402d-aa82-afd5d57eb5ab" />
<img width="386" height="856" alt="image" src="https://github.com/user-attachments/assets/cae9b512-b2c3-4002-aa37-d286d58c6cc0" />
<img width="391" height="868" alt="image" src="https://github.com/user-attachments/assets/fe3b8ca2-2723-4421-9f1b-3929ae5e53dc" />

