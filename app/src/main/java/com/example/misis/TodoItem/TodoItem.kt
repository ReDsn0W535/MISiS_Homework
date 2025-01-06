package com.example.misis.TodoItem

import java.time.OffsetDateTime


enum class TaskImportance {
    LOW,
    DEFAULT,
    HIGH
}

fun mapTaskImportanceToText(importance: TaskImportance): String
{
    return when (importance)
    {
        TaskImportance.LOW ->  "Низкий"
        TaskImportance.DEFAULT ->  "Средний"
        TaskImportance.HIGH ->  "Высокий"
    }
}

//@SuppressLint("NewApi")
data class TodoItem(
    val id: String,
    val bodyText: String,
    val importance: TaskImportance,
    val isDone: Boolean,
    val creationTime: OffsetDateTime,
    val deadline: OffsetDateTime?,
    val lastEditTime: OffsetDateTime?,
) {}