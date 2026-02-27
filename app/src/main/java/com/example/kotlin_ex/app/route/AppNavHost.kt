package com.example.kotlin_ex.app.route

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kotlin_ex.presentation.todo_detail.screen.TodoDetailScreen
import com.example.kotlin_ex.presentation.todo_list.screen.TodoScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.TodoList.path
    ) {
        composable(Route.TodoList.path) {
            TodoScreen(
                viewModel = hiltViewModel(),
                onTodoClick = { todoId ->
                    navController.navigate(RoutePath.todoDetail(todoId))
                }
            )
        }

        composable(
            route = Route.TodoDetail.path,
            arguments = listOf(
                navArgument("todoId") { type = NavType.LongType }
            )
        ) {
            TodoDetailScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
