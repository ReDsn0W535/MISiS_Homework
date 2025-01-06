package com.example.misis

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import com.example.misis.TodoItem.TaskImportance
import com.example.misis.TodoItem.TodoItem
import com.example.misis.TodoItemRepository.TodoItemRepository
import java.time.OffsetDateTime

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences : SharedPreferences = getPreferences(MODE_PRIVATE)
        TodoItemPreferences.initPreferences(preferences)
        val viewModel: TodoItemRepository =
            ViewModelProvider(this)[TodoItemRepository::class.java]

        val selectedItem : MutableState<TodoItem> = mutableStateOf(TodoItemData(""))
        val isEditorOpen = mutableStateOf(false)
        val openRedactor = { item : TodoItem? ->
            if (item == null){
                selectedItem.value= TodoItem("", "", TaskImportance.DEFAULT, false, OffsetDateTime.now(), null, null)
            }
            else{
                selectedItem.value = item
            }
            isEditorOpen.value = true
        }


        enableEdgeToEdge()
        setContent {
            ThemeUpdater(isDarkTheme = viewModel.isDarkTheme()) {
                TodoItemsScreen(viewModel,openRedactor)
                TodoItemEditor(selectedItem.value,viewModel,isEditorOpen)
            }
        }
}