package com.example.kotlin_ex.presentation.todo_list.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.kotlin_ex.domain.entity.Todo
import com.example.kotlin_ex.presentation.todo_list.effect.TodoSideEffect
import com.example.kotlin_ex.presentation.todo_list.event.TodoUiEvent
import com.example.kotlin_ex.presentation.todo_list.state.TodoFilter
import com.example.kotlin_ex.presentation.todo_list.state.TodoUiState
import com.example.kotlin_ex.presentation.todo_list.viewmodel.TodoViewModel

@Composable
fun TodoScreen(
    viewModel: TodoViewModel,
    onTodoClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // SideEffect 수집 (BLoC의 listener와 동일)
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is TodoSideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // --- 입력창 ---
            TodoInput(
                text = uiState.inputText,
                onTextChange = { viewModel.onEvent(TodoUiEvent.OnInputChanged(it)) },
                onAdd = { viewModel.onEvent(TodoUiEvent.OnAddTodo) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- 필터 ---
            TodoFilterBar(
                currentFilter = uiState.filter,
                onFilterChange = { viewModel.onEvent(TodoUiEvent.OnFilterChanged(it)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- 목록 ---
            val filteredTodos = when (uiState.filter) {
                TodoFilter.ALL -> uiState.todos
                TodoFilter.ACTIVE -> uiState.todos.filter { !it.isDone }
                TodoFilter.DONE -> uiState.todos.filter { it.isDone }
            }

            LazyColumn {
                items(filteredTodos, key = { it.id }) { todo ->
                    TodoItem(
                        todo = todo,
                        onClick = { onTodoClick(todo.id) },
                        onToggle = { viewModel.onEvent(TodoUiEvent.OnToggleTodo(todo.id)) },
                        onDelete = { viewModel.onEvent(TodoUiEvent.OnDeleteTodo(todo.id)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TodoInput(
    text: String,
    onTextChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("할 일을 입력하세요") },
            singleLine = true
        )
        IconButton(onClick = onAdd) {
            Icon(Icons.Default.Add, contentDescription = "추가")
        }
    }
}

@Composable
private fun TodoFilterBar(
    currentFilter: TodoFilter,
    onFilterChange: (TodoFilter) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TodoFilter.entries.forEach { filter ->
            FilterChip(
                selected = currentFilter == filter,
                onClick = { onFilterChange(filter) },
                label = {
                    Text(
                        when (filter) {
                            TodoFilter.ALL -> "전체"
                            TodoFilter.ACTIVE -> "미완료"
                            TodoFilter.DONE -> "완료"
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun TodoItem(
    todo: Todo,
    onClick: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = { onToggle() }
        )
        Text(
            text = todo.title,
            modifier = Modifier.weight(1f),
            textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "삭제")
        }
    }
}
