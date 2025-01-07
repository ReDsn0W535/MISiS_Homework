package com.example.misis.util.todoitem

import java.time.OffsetDateTime

enum class TaskImportance {
    LOW,
    DEFAULT,
    HIGH
}

fun mapTaskImportanceToText(importance: TaskImportance): String {
    return when (importance) {
        TaskImportance.LOW ->  "Низкая"
        TaskImportance.DEFAULT ->  "Средняя"
        TaskImportance.HIGH ->  "❗ Высокая"
    }
}

//@SuppressLint("NewApi")
data class TodoItem(
    val id: String,
    var bodyText: String,
    val importance: TaskImportance,
    val isDone: Boolean,
    val creationTime: OffsetDateTime,
    var deadline: OffsetDateTime?,
    val lastEditTime: OffsetDateTime?,
) {}