package com.example.kotlin_ex.presentation.common

sealed interface CommonSideEffect {
    data class ShowSnackbar(val message: String) : CommonSideEffect
    data class ShowToast(val message: String) : CommonSideEffect
    data object NavigateBack : CommonSideEffect
}
