package com.example.kotlin_ex.presentation.todo_detail.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.kotlin_ex.presentation.todo_detail.effect.TodoDetailSideEffect
import com.example.kotlin_ex.presentation.todo_detail.event.TodoDetailUiEvent
import com.example.kotlin_ex.presentation.todo_detail.viewmodel.TodoDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    viewModel: TodoDetailViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is TodoDetailSideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is TodoDetailSideEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("할 일 상세") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val todo = uiState.todo
            if (todo == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("할 일을 찾을 수 없습니다")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.headlineMedium,
                        textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (todo.isDone) "완료됨" else "미완료",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (todo.isDone) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { viewModel.onEvent(TodoDetailUiEvent.OnToggleTodo) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (todo.isDone) "미완료로 변경" else "완료로 변경")
                        }

                        Button(
                            onClick = { viewModel.onEvent(TodoDetailUiEvent.OnDeleteTodo) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("삭제")
                        }
                    }
                }
            }
        }
    }
}
