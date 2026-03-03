package com.example.kotlin_ex.presentation.todo_detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_ex.domain.usecase.DeleteTodoUseCase
import com.example.kotlin_ex.domain.usecase.GetTodoByIdUseCase
import com.example.kotlin_ex.domain.usecase.ToggleTodoUseCase
import com.example.kotlin_ex.presentation.common.CommonSideEffect
import com.example.kotlin_ex.presentation.todo_detail.effect.TodoDetailSideEffect
import com.example.kotlin_ex.presentation.todo_detail.event.TodoDetailUiEvent
import com.example.kotlin_ex.presentation.todo_detail.state.TodoDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTodoByIdUseCase: GetTodoByIdUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
) : ViewModel() {

    private val todoId: Long = checkNotNull(savedStateHandle["todoId"])

    private val _uiState = MutableStateFlow(TodoDetailUiState())
    val uiState: StateFlow<TodoDetailUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<TodoDetailSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        loadTodo()
    }

    fun onEvent(event: TodoDetailUiEvent) {
        when (event) {
            is TodoDetailUiEvent.OnToggleTodo -> toggleTodo()
            is TodoDetailUiEvent.OnDeleteTodo -> deleteTodo()
        }
    }

    private fun loadTodo() {
        viewModelScope.launch {
            val todo = getTodoByIdUseCase(todoId)
            _uiState.update { it.copy(todo = todo, isLoading = false) }
        }
    }

    private fun toggleTodo() {
        viewModelScope.launch {
            toggleTodoUseCase(todoId)
            val currentTodo = _uiState.value.todo ?: return@launch
            _uiState.update { it.copy(todo = currentTodo.copy(isDone = !currentTodo.isDone)) }
            _sideEffect.send(TodoDetailSideEffect.Common(CommonSideEffect.ShowSnackbar("상태가 변경되었습니다")))
        }
    }

    private fun deleteTodo() {
        viewModelScope.launch {
            deleteTodoUseCase(todoId)
            _sideEffect.send(TodoDetailSideEffect.Common(CommonSideEffect.NavigateBack))
        }
    }
}
