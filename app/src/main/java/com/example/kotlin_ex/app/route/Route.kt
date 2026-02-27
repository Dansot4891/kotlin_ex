package com.example.kotlin_ex.app.route

sealed class Route(val path: String) {
    data object TodoList : Route(RoutePath.TODO_LIST)
    data object TodoDetail : Route(RoutePath.TODO_DETAIL)
}
