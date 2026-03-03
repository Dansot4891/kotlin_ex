# Todo App — Kotlin + Compose + Clean Architecture

Flutter 개발자가 Android 네이티브를 학습하기 위해 만든 Todo 앱.
Clean Architecture + MVVM/MVI 패턴으로 구성되어 있다.

---

## Tech Stack

| 분류 | 기술 |
|------|------|
| Language | Kotlin |
| UI | Jetpack Compose (Material3) |
| 상태 관리 | ViewModel + StateFlow + Channel |
| 아키텍처 | Clean Architecture (3-layer) + MVI |
| DI | Hilt (Dagger) |
| 비동기 | Kotlin Coroutines / Flow |
| 네비게이션 | Navigation Compose |

---

## 아키텍처 개요

```
┌─────────────────────────────────────────────────┐
│                 Presentation                     │
│  Screen ─→ ViewModel ─→ UiState / SideEffect    │
│         ←─ onEvent(UiEvent)                      │
└──────────────────────┬──────────────────────────┘
                       │ UseCase 호출
┌──────────────────────▼──────────────────────────┐
│                   Domain                         │
│  UseCase ─→ Repository (interface)               │
│  Entity (Todo)                                   │
└──────────────────────┬──────────────────────────┘
                       │ 구현체 주입 (DIP)
┌──────────────────────▼──────────────────────────┐
│                    Data                          │
│  RepositoryImpl ─→ DataSource (interface)        │
│  Model (TodoModel) / Mapper                      │
│  FakeTodoLocalDataSource (mock 구현체)            │
└─────────────────────────────────────────────────┘
```

### 레이어 규칙

- **Presentation** → Domain 참조 가능, Data 참조 불가
- **Domain** → 어디도 참조하지 않음 (순수 Kotlin)
- **Data** → Domain 참조 가능 (Repository interface 구현을 위해)

---

## 패키지 구조

```
com.example.kotlin_ex/
│
├── app/                          ← 앱 설정
│   ├── di/
│   │   └── AppModule.kt         ← Hilt DI 모듈
│   └── route/
│       ├── RoutePath.kt          ← 경로 상수
│       ├── Route.kt              ← sealed class 라우트 정의
│       └── AppNavHost.kt         ← NavHost 설정
│
├── domain/                       ← 비즈니스 로직 (순수 Kotlin)
│   ├── entity/
│   │   └── Todo.kt               ← 핵심 엔티티
│   ├── repository/
│   │   └── TodoRepository.kt     ← Repository 인터페이스
│   └── usecase/
│       ├── GetTodosUseCase.kt
│       ├── GetTodoByIdUseCase.kt
│       ├── AddTodoUseCase.kt
│       ├── ToggleTodoUseCase.kt
│       └── DeleteTodoUseCase.kt
│
├── data/                         ← 데이터 접근
│   ├── model/
│   │   └── TodoModel.kt          ← Data 전용 모델
│   ├── mapper/
│   │   └── TodoMapper.kt         ← TodoModel <-> Todo 변환
│   ├── datasource/
│   │   ├── TodoLocalDataSource.kt       ← 인터페이스
│   │   └── FakeTodoLocalDataSource.kt   ← mock 구현체
│   └── repository/
│       └── TodoRepositoryImpl.kt  ← TodoRepository 구현
│
├── presentation/                 ← UI + 상태 관리
│   ├── todo_list/
│   │   ├── state/
│   │   │   └── TodoUiState.kt    ← 화면 상태
│   │   ├── event/
│   │   │   └── TodoUiEvent.kt    ← 사용자 액션
│   │   ├── effect/
│   │   │   └── TodoSideEffect.kt ← 일회성 이벤트
│   │   ├── viewmodel/
│   │   │   └── TodoViewModel.kt
│   │   └── screen/
│   │       └── TodoScreen.kt
│   └── todo_detail/
│       ├── state/
│       │   └── TodoDetailUiState.kt
│       ├── event/
│       │   └── TodoDetailUiEvent.kt
│       ├── effect/
│       │   └── TodoDetailSideEffect.kt
│       ├── viewmodel/
│       │   └── TodoDetailViewModel.kt
│       └── screen/
│           └── TodoDetailScreen.kt
│
├── ui/theme/                     ← Material3 테마
├── MainActivity.kt               ← @AndroidEntryPoint
└── TodoApplication.kt            ← @HiltAndroidApp
```

---

## MVI 패턴 흐름

```
사용자 탭
  ↓
Screen → onEvent(UiEvent)
  ↓
ViewModel
  ├─ when(event) 분기 처리
  ├─ UseCase 호출 (suspend)
  ├─ _uiState.update { ... }     → UI 자동 갱신 (StateFlow)
  └─ _sideEffect.send(...)       → 일회성 이벤트 (Channel)
  ↓
Screen
  ├─ collectAsState()             → UI 렌더링
  └─ LaunchedEffect { collect }   → Snackbar, Navigation 등
```

### 세 요소의 역할

| 요소 | 용도 | 예시 |
|------|------|------|
| **UiState** | 화면에 계속 보이는 데이터 | Todo 목록, 입력 텍스트, 필터 |
| **UiEvent** | 사용자가 할 수 있는 액션 | 추가, 삭제, 토글, 필터 변경 |
| **SideEffect** | 한 번만 실행할 이벤트 | Snackbar, 화면 이동 |

---

## 데이터 흐름 (전체)

```
TodoScreen
  → TodoViewModel
    → AddTodoUseCase(todo: Todo)
      → TodoRepository.addTodo(todo)        [domain interface]
        → TodoRepositoryImpl.addTodo(todo)
          → todo.toData()                    [Mapper: Entity → Model]
            → FakeTodoLocalDataSource.addTodo(todoModel)
              → MutableStateFlow 업데이트
                → Flow 발행
                  → TodoRepositoryImpl.getTodos()
                    → .map { it.toDomain() } [Mapper: Model → Entity]
                      → TodoViewModel._uiState.update
                        → TodoScreen 자동 갱신
```

---

## 핵심 개념 — Flutter 비교

### 상태 관리

| Flutter | Android (이 프로젝트) |
|---------|----------------------|
| BLoC / Riverpod 등 서드파티 | ViewModel + StateFlow (공식 표준) |
| `Stream<State>` | `StateFlow<UiState>` |
| `BlocBuilder` | `collectAsState()` |
| `BlocListener` | `LaunchedEffect { sideEffect.collect }` |
| `add(Event)` | `onEvent(UiEvent)` |

### 비동기

| Flutter | Android |
|---------|---------|
| `async` / `await` | `suspend` |
| `Future<T>` | 코루틴 (자동 처리) |
| `Stream<T>` | `Flow<T>` |
| `StreamController` | `MutableStateFlow` |

### DI

| Flutter | Android |
|---------|---------|
| `get_it` + `injectable` | Hilt (`@Module` + `@Provides`) |
| `@singleton` | `@Singleton` |
| `@injectable` | `@Inject constructor` |

### 네비게이션

| Flutter | Android |
|---------|---------|
| `go_router` | Navigation Compose |
| `context.go('/detail/1')` | `navController.navigate("todo_detail/1")` |
| `context.pop()` | `navController.popBackStack()` |
| `GoRoute(path:)` | `composable(route:)` |
| `state.pathParameters['id']` | `SavedStateHandle["todoId"]` |

### 파일/폴더 네이밍

| | Flutter | Android |
|--|---------|---------|
| 파일명 | `todo_screen.dart` (snake_case) | `TodoScreen.kt` (PascalCase) |
| 폴더명 | `todo_detail/` (snake_case) | `todo_detail/` (snake_case 또는 소문자) |
| 클래스명 | `TodoScreen` (PascalCase) | `TodoScreen` (PascalCase) |

---

## 기능 목록

- [x] Todo 목록 조회
- [x] Todo 추가 (텍스트 입력)
- [x] Todo 완료 토글 (체크박스)
- [x] Todo 삭제
- [x] 필터: 전체 / 미완료 / 완료
- [x] 상세 화면 (조회 / 토글 / 삭제)
- [x] 화면 이동 (Navigation Compose)
- [x] Snackbar 알림 (SideEffect)
- [ ] Room DB 연동 (미구현 — FakeDataSource를 교체하면 됨)
- [ ] 검색 기능
- [ ] 수정 기능

---

## 확장 가이드

### Room DB 도입 시

1. `data/model/TodoModel.kt`에 `@Entity` 어노테이션 추가
2. `data/datasource/RoomTodoLocalDataSource.kt` 생성 (DAO 활용)
3. `AppModule.kt`에서 `FakeTodoLocalDataSource` → `RoomTodoLocalDataSource`로 교체

Domain, Presentation 레이어는 **변경 없음**.

### Remote API 연동 시

1. `data/datasource/RemoteTodoDataSource.kt` 추가 (Retrofit)
2. `data/model/TodoDto.kt` 추가 (API 응답 모델)
3. `TodoMapper.kt`에 `TodoDto.toDomain()` 추가
4. `TodoRepositoryImpl`에서 Local + Remote 조합 로직 구현

Domain, Presentation 레이어는 **변경 없음**.
