package com.todo.todoItemsVievModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.todo.dataStorage.DataStorage
import com.todo.todoItem.TodoItem


// мяу мяу мяу поменять имя
class TodoItemsRepository() : ViewModel() {
    private val _todoItems: MutableList<TodoItem> = mutableStateListOf();

    init {
        val storedItems = DataStorage.getItems()
        if (storedItems.isNotEmpty()) {
            _todoItems.addAll(storedItems)
        }
    }

    public fun getTodoItems(): List<TodoItem> = _todoItems;

    public fun setTodoItem(item: TodoItem) {
        _todoItems.add(item)
        DataStorage.writeItem(item)
    }

    public fun updateTodoItem(updatedItem: TodoItem) {
        val index = _todoItems.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            _todoItems[index] = updatedItem
            DataStorage.writeItem(updatedItem)
        }
    }

    public fun deleteTodoItem(itemId: String) {
        _todoItems.removeAll { it.id == itemId }
        DataStorage.deleteItem(itemId)
    }
}