package com.example.misis.taskStorage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.example.misis.TodoItem.TodoItem
import com.google.gson.reflect.TypeToken

object TaskStorage {
    private var preferences: SharedPreferences? = null
    private var gson: Gson = Gson()

    fun init(inPreferences: SharedPreferences) {
        preferences = inPreferences
    }

    fun saveTasks(tasks: List<TodoItem>) {
        preferences?.edit()?.putString("Tasks", gson.toJson(tasks))?.apply()
    }

    fun retrieveTasks(): List<TodoItem> {
        return preferences?.getString("Tasks", null)?.let { json ->
            val type = object : TypeToken<List<TodoItem>>() {}.type
            gson.fromJson<List<TodoItem>>(json, type) ?: emptyList()
        } ?: emptyList()
    }

    fun saveTask(task: TodoItem) {
        val tasks = retrieveTasks().toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id}
        if (index != -1)
        {
            tasks[index] = task
        }
        else
        {
            tasks.add(task)
        }
        saveTasks(tasks)
    }

    fun deleteTask(taskId: String)
    {
        saveTasks(retrieveTasks().filter { it.id != taskId })
    }
}