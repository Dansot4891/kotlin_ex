package com.example.kotlin_ex.data.model

data class TodoModel(
    val id: Long,
    val title: String,
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
