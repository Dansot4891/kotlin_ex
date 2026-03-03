package com.example.kotlin_ex.data.datasource

import com.example.kotlin_ex.data.model.TodoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class FakeTodoLocalDataSource : TodoLocalDataSource {

    private val mutex = Mutex()

    private val todos = MutableStateFlow(
        listOf(
            TodoModel(id = 1, title = "Kotlin 기초 공부", isDone = false),
            TodoModel(id = 2, title = "Compose UI 만들기", isDone = false),
            TodoModel(id = 3, title = "Clean Architecture 이해하기", isDone = true),
        )
    )

    override fun getTodos(): Flow<List<TodoModel>> = todos.asStateFlow()

    override suspend fun getTodoById(id: Long): TodoModel? {
        return todos.value.firstOrNull { it.id == id }
    }

    override suspend fun addTodo(todo: TodoModel) = mutex.withLock {
        todos.update { current -> current + todo }
    }

    override suspend fun toggleTodo(id: Long) = mutex.withLock {
        todos.update { current ->
            current.map { if (it.id == id) it.copy(isDone = !it.isDone) else it }
        }
    }

    override suspend fun deleteTodo(id: Long) = mutex.withLock {
        todos.update { current -> current.filter { it.id != id } }
    }
}
