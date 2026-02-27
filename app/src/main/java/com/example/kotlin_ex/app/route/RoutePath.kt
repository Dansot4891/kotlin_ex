package com.example.kotlin_ex.app.route

object RoutePath {
    const val TODO_LIST = "todo_list"
    const val TODO_DETAIL = "todo_detail/{todoId}"

    fun todoDetail(todoId: Long) = "todo_detail/$todoId"
}
