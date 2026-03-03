package com.example.kotlin_ex.presentation.todo_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_ex.domain.entity.Todo
import com.example.kotlin_ex.domain.usecase.AddTodoUseCase
import com.example.kotlin_ex.domain.usecase.DeleteTodoUseCase
import com.example.kotlin_ex.domain.usecase.GetTodosUseCase
import com.example.kotlin_ex.domain.usecase.ToggleTodoUseCase
import com.example.kotlin_ex.presentation.todo_list.effect.TodoSideEffect
import com.example.kotlin_ex.presentation.todo_list.event.TodoUiEvent
import com.example.kotlin_ex.presentation.todo_list.state.TodoFilter
import com.example.kotlin_ex.presentation.todo_list.state.TodoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
) : ViewModel() {

    private val nextId = AtomicLong(0)

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    // --- SideEffect (일회성 이벤트 — Channel로 한 번만 소비) ---
    private val _sideEffect = Channel<TodoSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        observeTodos()
        initNextId()
    }

    private fun initNextId() {
        viewModelScope.launch {
            getTodosUseCase().collect { todos ->
                val maxId = todos.maxOfOrNull { it.id } ?: 0
                nextId.compareAndSet(0, maxId)
            }
        }
    }

    // --- Event 처리 (BLoC의 on<Event>와 동일) ---
    fun onEvent(event: TodoUiEvent) {
        when (event) {
            is TodoUiEvent.OnInputChanged -> {
                _uiState.update { it.copy(inputText = event.text) }
            }

            is TodoUiEvent.OnAddTodo -> addTodo()

            is TodoUiEvent.OnToggleTodo -> toggleTodo(event.id)

            is TodoUiEvent.OnDeleteTodo -> deleteTodo(event.id)

            is TodoUiEvent.OnFilterChanged -> {
                _uiState.update { it.copy(filter = event.filter) }
            }
        }
    }

    private fun observeTodos() {
        viewModelScope.launch {
            getTodosUseCase().collect { todos ->
                _uiState.update { it.copy(todos = todos) }
            }
        }
    }

    private fun addTodo() {
        val title = _uiState.value.inputText.trim()
        if (title.isBlank()) {
            viewModelScope.launch {
                _sideEffect.send(TodoSideEffect.ShowSnackbar("할 일을 입력해주세요"))
            }
            return
        }

        viewModelScope.launch {
            addTodoUseCase(Todo(id = nextId.incrementAndGet(), title = title))
            _uiState.update { it.copy(inputText = "") }
            _sideEffect.send(TodoSideEffect.ShowSnackbar("추가되었습니다"))
        }
    }

    private fun toggleTodo(id: Long) {
        viewModelScope.launch {
            toggleTodoUseCase(id)
        }
    }

    private fun deleteTodo(id: Long) {
        viewModelScope.launch {
            deleteTodoUseCase(id)
            _sideEffect.send(TodoSideEffect.ShowSnackbar("삭제되었습니다"))
        }
    }
}
