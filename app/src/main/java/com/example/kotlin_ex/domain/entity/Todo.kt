package com.example.kotlin_ex.domain.entity

data class Todo(
    val id: Long,
    val title: String,
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
