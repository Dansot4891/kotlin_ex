package com.example.kotlin_ex.app.di

import com.example.kotlin_ex.data.datasource.FakeTodoLocalDataSource
import com.example.kotlin_ex.data.datasource.TodoLocalDataSource
import com.example.kotlin_ex.data.repository.TodoRepositoryImpl
import com.example.kotlin_ex.domain.repository.TodoRepository
import com.example.kotlin_ex.domain.usecase.AddTodoUseCase
import com.example.kotlin_ex.domain.usecase.DeleteTodoUseCase
import com.example.kotlin_ex.domain.usecase.GetTodosUseCase
import com.example.kotlin_ex.domain.usecase.ToggleTodoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- Data Layer ---

    @Provides
    @Singleton
    fun provideTodoLocalDataSource(): TodoLocalDataSource {
        return FakeTodoLocalDataSource()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(localDataSource: TodoLocalDataSource): TodoRepository {
        return TodoRepositoryImpl(localDataSource)
    }

    // --- Domain Layer (UseCases) ---

    @Provides
    fun provideGetTodosUseCase(repository: TodoRepository): GetTodosUseCase {
        return GetTodosUseCase(repository)
    }

    @Provides
    fun provideAddTodoUseCase(repository: TodoRepository): AddTodoUseCase {
        return AddTodoUseCase(repository)
    }

    @Provides
    fun provideToggleTodoUseCase(repository: TodoRepository): ToggleTodoUseCase {
        return ToggleTodoUseCase(repository)
    }

    @Provides
    fun provideDeleteTodoUseCase(repository: TodoRepository): DeleteTodoUseCase {
        return DeleteTodoUseCase(repository)
    }
}
