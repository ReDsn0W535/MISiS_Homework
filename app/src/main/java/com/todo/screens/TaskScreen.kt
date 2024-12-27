package com.todo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.todo.components.DeadlinePicker

import com.todo.todoItem.Priorities
//import com.todo.Priorities
import com.todo.todoItem.TodoItem
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    onTaskAdd: (TodoItem) -> Unit,
    onCanel: () -> Unit,
    initialTask: TodoItem? = null,
    onUpdate: (TodoItem) -> Unit
) {

    var taskText by remember { mutableStateOf(initialTask?.text ?: "") }
    var selectedPriority by remember { mutableStateOf(initialTask?.importance ?: Priorities.MID) }
    var deadline by remember { mutableStateOf(initialTask?.deadline) }
    var expanded by remember { mutableStateOf(false) }

    var isError by remember { mutableStateOf(false) }


    var TempItem: TodoItem =
        TodoItem(
            id = initialTask?.id ?: System.currentTimeMillis().toString(),
            text = taskText,
            importance = selectedPriority,
            deadline = deadline,
            isCompleted = false,
            createdAt = initialTask?.createdAt ?: Date(),
            modifiedAt = Date()
        );

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(
                        top = 1.dp,
                        bottom = 1.dp,
                        start = 3.dp,
                        end = 3.dp
                    ),
                    onClick = {
                        if (taskText.isBlank()) {
                            isError = true
                        } else {
                            isError = false
                            if (initialTask == null) {
                                onTaskAdd(TempItem)
                            } else {
                                onUpdate(TempItem)
                            }
                        }
                    },
                ) {
                    Text(text = "Готово", color = MaterialTheme.colorScheme.surfaceVariant)
                }
                Text(
                    text = if (initialTask != null) "Редактирование задачи" else "Добавление задачи",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.displayMedium,

                    )
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(
                        top = 1.dp,
                        bottom = 1.dp,
                        start = 3.dp,
                        end = 3.dp
                    ),
                    onClick = onCanel,
                ) {
                    Text(text = "Отмена", color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }


            TextField(
                value = taskText,
                onValueChange = { taskText = it },
                colors = textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    errorTextColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    errorContainerColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.onSecondary,

                    ),
                label = { Text(text = "Описание задачи") },
                isError = isError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(5.dp)
                    )
            )

            if (isError) {
                Text(
                    text = "Описание не может быть пустым",
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }



            Text(text = "Важность:", color = MaterialTheme.colorScheme.onSecondary)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = when (selectedPriority) {
                        Priorities.LOW -> "Низкий"
                        Priorities.MID -> "Средний"
                        Priorities.HIGH -> "Высокий"
                    },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
                    onDismissRequest = { expanded = false }) {
                    Priorities.values().forEachIndexed { index, priority ->
                        val priorityText = when (priority) {
                            Priorities.LOW -> "Низкий"
                            Priorities.MID -> "Средний"
                            Priorities.HIGH -> "Высокий"
                        }
                        DropdownMenuItem(

                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.primary),
                            text = {
                                Text(
                                    text = priorityText,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            onClick = {
                                selectedPriority = priority
                                expanded = false
                            }
                        )
                        if (index < Priorities.values().lastIndex) {
                            Divider(
                                color = MaterialTheme.colorScheme.outline,
                                thickness = 1.dp,
                            )
                        }
                    }
                }
            }

            Text(text = "Выберите дедлайн:", color = MaterialTheme.colorScheme.onSecondary)
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DeadlinePicker(
                        initialDate = deadline,
                        onDateSelected = { selectedDate -> deadline = selectedDate }
                    )
                }
            }
        }
    }
}