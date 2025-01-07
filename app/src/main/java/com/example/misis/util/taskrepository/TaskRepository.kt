package com.example.misis.util.taskrepository

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.misis.util.todoitem.TodoItem
import com.example.misis.util.taskStorage.TaskStorage

class TaskRepository : ViewModel() {

    private val tasks = mutableStateListOf<TodoItem>()

    fun createTask(task: TodoItem) {
        tasks.add(task)
        TaskStorage.saveTask(task)
    }

    fun getAllTasks(): List<TodoItem> {
        return tasks
    }

    fun getFirstTask(): TodoItem? {
        return tasks.firstOrNull()
    }

    fun addOrUpdateTask(task: TodoItem) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        } else {
            tasks.add(task)
        }
        TaskStorage.saveTask(task)
    }

    fun updateTask(updatedTask: TodoItem) {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            tasks[index] = updatedTask
            TaskStorage.saveTask(updatedTask)
        }
    }

    fun removeTask(task: TodoItem) {
        tasks.removeAll { it.id == task.id }
        TaskStorage.deleteTask(task.id)
    }

    fun removeTaskById(taskId: String) {
        tasks.removeAll { it.id == taskId }
        TaskStorage.deleteTask(taskId)
    }
}