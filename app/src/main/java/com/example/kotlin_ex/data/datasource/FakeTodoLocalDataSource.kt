package com.example.kotlin_ex.data.datasource

import com.example.kotlin_ex.data.model.TodoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeTodoLocalDataSource : TodoLocalDataSource {

    private val todos = MutableStateFlow(
        mutableListOf(
            TodoModel(id = 1, title = "Kotlin 기초 공부", isDone = false),
            TodoModel(id = 2, title = "Compose UI 만들기", isDone = false),
            TodoModel(id = 3, title = "Clean Architecture 이해하기", isDone = true),
        )
    )

    override fun getTodos(): Flow<List<TodoModel>> = todos.asStateFlow()

    override suspend fun getTodoById(id: Long): TodoModel? {
        return todos.value.firstOrNull { it.id == id }
    }

    override suspend fun addTodo(todo: TodoModel) {
        val current = todos.value.toMutableList()
        current.add(todo)
        todos.value = current
    }

    override suspend fun toggleTodo(id: Long) {
        val current = todos.value.toMutableList()
        val index = current.indexOfFirst { it.id == id }
        if (index != -1) {
            current[index] = current[index].copy(isDone = !current[index].isDone)
            todos.value = current
        }
    }

    override suspend fun deleteTodo(id: Long) {
        val current = todos.value.toMutableList()
        current.removeAll { it.id == id }
        todos.value = current
    }
}
