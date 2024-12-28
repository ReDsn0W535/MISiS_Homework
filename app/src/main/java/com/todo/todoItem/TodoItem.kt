package com.todo.todoItem

//import com.todo.Priorities
import java.util.Date

enum class Priorities {
    LOW,
    MID,
    HIGH
}


data class TodoItem(
    val id: String,
    val text: String = "",
    val importance: Priorities = Priorities.MID,
    val deadline: Date?,
    val isCompleted: Boolean = false,
    val createdAt: Date = Date(),
    val modifiedAt: Date?
)