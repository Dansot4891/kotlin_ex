package com.example.kotlin_ex.presentation.todo_detail.effect

import com.example.kotlin_ex.presentation.common.CommonSideEffect

sealed interface TodoDetailSideEffect {
    data class Common(val effect: CommonSideEffect) : TodoDetailSideEffect
}
