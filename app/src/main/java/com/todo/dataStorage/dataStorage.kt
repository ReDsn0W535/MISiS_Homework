package com.todo.dataStorage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.todo.todoItem.TodoItem

object DataStorage {
    private var prefs: SharedPreferences? = null
    private val gson = Gson()

    fun init(preferences: SharedPreferences) {
        prefs = preferences
    }

    fun writeItems(items: List<TodoItem>) {
        prefs?.edit()?.putString("TODO_ITEMS", gson.toJson(items))?.apply()
    }

    fun getItems(): List<TodoItem> {
        return prefs?.getString("TODO_ITEMS", null)?.let { json ->
            val type = object : TypeToken<List<TodoItem>>() {}.type
            gson.fromJson<List<TodoItem>>(json, type) ?: emptyList()
        } ?: emptyList()
    }

    fun writeItem(item: TodoItem) {
        val items = getItems().toMutableList()
        val index = items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            items[index] = item
        } else {
            items.add(item)
        }
        writeItems(items)
    }

    fun deleteItem(itemId: String) {
        writeItems(getItems().filter { it.id != itemId })
    }
}