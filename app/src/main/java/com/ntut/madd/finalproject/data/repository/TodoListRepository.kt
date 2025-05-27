package com.ntut.madd.finalproject.data.repository

import com.ntut.madd.finalproject.data.datasource.TodoListRemoteDataSource
import com.ntut.madd.finalproject.data.model.TodoList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoListRepository @Inject constructor(
    private val todoListRemoteDataSource: TodoListRemoteDataSource
) {
    val todoLists: Flow<List<TodoList>> = todoListRemoteDataSource.getTodoLists("")

    suspend fun getTodoList(listId: String): TodoList? {
       return todoListRemoteDataSource.getTodoList(listId)
    }

    suspend fun create(todoList: TodoList): String {
        return todoListRemoteDataSource.create(todoList)
    }

    suspend fun update(todoList: TodoList) {
        todoListRemoteDataSource.update(todoList)
    }

    suspend fun delete(listId: String) {
        todoListRemoteDataSource.delete(listId)
    }
}