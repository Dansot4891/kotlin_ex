package com.example.kotlin_ex.app.di

import com.example.kotlin_ex.data.datasource.FakeTodoLocalDataSource
import com.example.kotlin_ex.data.datasource.TodoLocalDataSource
import com.example.kotlin_ex.data.repository.TodoRepositoryImpl
import com.example.kotlin_ex.domain.repository.TodoRepository
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
}
