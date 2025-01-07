package com.example.misis.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.misis.util.todoitem.TodoItem
import com.example.misis.ui.common.task.TaskUI


@Composable
fun TodoListScreen(
    todoItems: List<TodoItem>,
    modifier: Modifier = Modifier,
    onAddItemClick: () -> Unit,
    onAddItem: (TodoItem) -> Unit = {},
    onComplete: (String, Boolean) -> Unit = { _, _ -> },
    onEdit: (TodoItem) -> Unit = {},
    onDelete: (String) -> Unit,
    isDarkTheme: MutableState<Boolean>
) {
    val isExpandedCompletedList = remember { mutableStateOf(true) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpandedCompletedList.value) 180f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    val isAddButtonRotated = remember { mutableStateOf(false) }
    val addButtonRotation by animateFloatAsState(
        targetValue = if (isAddButtonRotated.value) 45f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        floatingActionButton = {
            FloatingActionButton(

                onClick = {
                    isAddButtonRotated.value = !isAddButtonRotated.value
                    onAddItemClick()
                },
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить",
                    modifier = Modifier
                        .size(48.dp)
                        .rotate(addButtonRotation)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Row(
                modifier = Modifier
                    .padding(top = 32.dp, start = 60.dp, end = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    text = "Мои дела",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 45.dp, top = 10.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "Выполнено — ${todoItems.count { it.isDone }}",
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .weight(1f)
                )

            }

            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                val sortedItems = todoItems.sortedByDescending { it.isDone }
                items(sortedItems, key = { it.id }) { item ->
                    if (!item.isDone || isExpandedCompletedList.value) {
                        TaskUI(
                            item = item,
                            onComplete = { isDone -> onComplete(item.id, isDone) },
                            onEdit = { task -> onEdit(task) },
                            onDelete = { id -> onDelete(id) }
                        )
                    }
                }
            }
        }
    }
}