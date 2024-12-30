package com.example.todoapp

import java.time.LocalDate

enum class ImportanceField(val st:String) {
    LOW("low"),
    MIDDLE("mid"),
    HIGH("high")
}

data class ToDoItem(
    var id: String? = null,
    var text : String = "",
    var importance: String = ImportanceField.LOW.st,
    var deadline: String? = null,
    var isDone : Boolean = false,
    var creationDate : String? = null,
    var addDate : String? = null
)

