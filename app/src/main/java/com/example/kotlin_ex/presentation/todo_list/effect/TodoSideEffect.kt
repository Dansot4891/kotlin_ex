package com.example.kotlin_ex.presentation.todo_list.effect

import com.example.kotlin_ex.presentation.common.CommonSideEffect

sealed interface TodoSideEffect {
    data class Common(val effect: CommonSideEffect) : TodoSideEffect
}
