package com.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import com.todo.ui.theme.RomadovaTODOTheme


//import com.google.gson.Gson
//import java.util.Date
import android.content.SharedPreferences
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
//import com.google.gson.reflect.TypeToken
import com.todo.dataStorage.DataStorage
import com.todo.router.AppNavigator
import com.todo.todoItemsVievModel.TodoItemsRepository


class MainActivity : ComponentActivity() {
    private val prefs: SharedPreferences by lazy { getPreferences(MODE_PRIVATE) }
    val repository: TodoItemsRepository by viewModels()
    private val isDarkTheme = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataStorage.init(prefs)

        enableEdgeToEdge()
        setContent {
            RomadovaTODOTheme(darkTheme = isDarkTheme.value) {
                AppNavigator(repository = repository, isDarkTheme)
            }
        }
    }
}



