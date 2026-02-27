package com.example.kotlin_ex.data.mapper

import com.example.kotlin_ex.data.model.TodoModel
import com.example.kotlin_ex.domain.entity.Todo

fun TodoModel.toDomain(): Todo = Todo(
    id = id,
    title = title,
    isDone = isDone,
    createdAt = createdAt
)

fun Todo.toData(): TodoModel = TodoModel(
    id = id,
    title = title,
    isDone = isDone,
    createdAt = createdAt
)
