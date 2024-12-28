package com.todo.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.todo.screens.TaskScreen
import com.todo.screens.TodoListScreen
import com.todo.todoItemsVievModel.TodoItemsRepository

@Composable
fun AppNavigator(repository: TodoItemsRepository, isDarkTheme: MutableState<Boolean>) {
    val navigator = rememberNavController();

    NavHost(navController = navigator, startDestination = "TODO_LIST") {
        composable("TODO_LIST") {
            TodoListScreen(
                todoItems = repository.getTodoItems(),
                onAddItemClick = { navigator.navigate("ADD_TASK") },
                onComplete = { id, isCompleted ->
                    val item = repository.getTodoItems().find { it.id == id }
                    if (item != null) {
                        val updatedItem = item.copy(isCompleted = isCompleted)
                        repository.updateTodoItem(updatedItem)
                    }
                },
                onAddItem = { newItem ->
                    repository.setTodoItem(newItem)
                },
                onEdit = { task ->
                    navigator.navigate("EDIT_TASK/${task.id}")
                },
                onDelete = { id ->
                    repository.deleteTodoItem(id)
                },
                isDarkTheme = isDarkTheme
            )
        }
        composable("ADD_TASK") {
            TaskScreen(
                onTaskAdd = { newItem ->
                    repository.setTodoItem(newItem)
                    navigator.popBackStack()
                },
                onCanel = {
                    navigator.popBackStack()
                },
                onUpdate = { newItem ->
                    repository.updateTodoItem(newItem)
                    navigator.popBackStack()
                }
            )
        }

        composable("EDIT_TASK/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            val task = repository.getTodoItems().find { it.id == taskId }
            task?.let {
                TaskScreen(
                    onTaskAdd = { updatedTask ->
                        repository.updateTodoItem(updatedTask)
                        navigator.popBackStack()
                    },
                    onCanel = {
                        navigator.popBackStack()
                    },
                    onUpdate = { newItem ->
                        repository.updateTodoItem(newItem)
                        navigator.popBackStack()
                    },
                    initialTask = it
                )
            }
        }
    }
}