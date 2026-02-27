package com.example.kotlin_ex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlin_ex.presentation.todo_list.screen.TodoScreen
import com.example.kotlin_ex.ui.theme.Kotlin_exTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kotlin_exTheme {
                TodoScreen(viewModel = hiltViewModel())
            }
        }
    }
}
