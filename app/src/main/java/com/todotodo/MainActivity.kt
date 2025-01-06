package com.todotodo

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import com.todotodo.screens.TodoItemEditor
import com.todotodo.screens.TodoItemsScreen
import com.todotodo.tododatapreferences.TodoItemPreferences
import com.todotodo.todoitem.TodoItemData
import com.todotodo.todoitemsviewmodel.TodoItemsViewModel
import com.todotodo.theme.ThemeUpdater

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences : SharedPreferences = getPreferences(MODE_PRIVATE)
        TodoItemPreferences.initPreferences(preferences)
        val viewModel: TodoItemsViewModel =
            ViewModelProvider(this).get(TodoItemsViewModel::class.java)

        val selectedItem : MutableState<TodoItemData> = mutableStateOf(TodoItemData(""))
        val isEditorOpen = mutableStateOf(false)
        val openRedactor = { item : TodoItemData? ->
            if(item==null){
                selectedItem.value= TodoItemData(id=viewModel.getFreeId())
            }
            else{
                selectedItem.value=item
            }
            isEditorOpen.value=true
        }


        enableEdgeToEdge()
        setContent {
            ThemeUpdater(isDarkTheme = viewModel.isDarkTheme()) {
                TodoItemsScreen(viewModel,openRedactor)
                TodoItemEditor(selectedItem.value,viewModel,isEditorOpen)
            }
        }
    }
}









