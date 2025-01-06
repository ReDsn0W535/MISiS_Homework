package com.todotodo.todoitem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.Date

enum class TodoPriority {
    LOW,
    NORMAL,
    HIGH
}

data class TodoItemData(val id: String,
                        val text: String = "New task",
                        var priority: TodoPriority = TodoPriority.NORMAL,
                        var isCompleted: Boolean = false,
                        var deadLine : Date?=null,
                        val createdAt: Date = Date(0),
                        var modifiedAt: Date?=null,
                        var nextId : String = "");


fun updateDate( item : TodoItemData) : TodoItemData {
    return if(item.createdAt.time==0L){
        item.copy(createdAt = Date())
    }
    else{
        item.copy(modifiedAt = Date())
    }
}


@Composable
fun getPriorityColor(priority: TodoPriority) : Color {
    return when(priority){
        TodoPriority.LOW -> MaterialTheme.colorScheme.surfaceContainerLow
        TodoPriority.NORMAL -> MaterialTheme.colorScheme.outlineVariant
        TodoPriority.HIGH -> MaterialTheme.colorScheme.surfaceContainerHigh
    }
}


fun getPriorityText(priority: TodoPriority) : String {
    return when(priority){
        TodoPriority.LOW -> "Низкий"
        TodoPriority.NORMAL -> "Средний"
        TodoPriority.HIGH -> "Высокий"
    }
}