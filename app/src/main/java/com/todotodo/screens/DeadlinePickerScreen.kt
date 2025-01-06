package com.todotodo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.todotodo.todoitem.TodoItemData
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadLinePicker(
    item : MutableState<TodoItemData>,
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = item.value.deadLine?.time)
    val visible = remember { mutableStateOf(false) }

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        label = { Text("Дедлайн") },
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary
        ),
        trailingIcon = {
            IconButton(onClick = { visible.value = !visible.value }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.95f)
    )

    AnimatedVisibility(visible.value) {
        DatePickerDialog(
            onDismissRequest = {visible.value=false},
            confirmButton = {
                TextButton(onClick = {
                    item.value = item.value.copy(deadLine= if(datePickerState.selectedDateMillis!= null) Date(datePickerState.selectedDateMillis!!) else null)
                    visible.value=false
                }) {
                    Text("Ок", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { visible.value = false }) {
                    Text("Отмена", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            DatePicker(state = datePickerState,colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondary,
                dividerColor = MaterialTheme.colorScheme.outlineVariant,
                dayContentColor = MaterialTheme.colorScheme.onPrimary,
                todayContentColor = MaterialTheme.colorScheme.onPrimary,
                todayDateBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                selectedDayContentColor = MaterialTheme.colorScheme.primary,
                selectedDayContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                selectedYearContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                yearContentColor = MaterialTheme.colorScheme.onPrimary,
                disabledSelectedYearContainerColor = MaterialTheme.colorScheme.outlineVariant,
                dateTextFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                ),
                weekdayContentColor = MaterialTheme.colorScheme.onPrimary
            ))
        }
    }
}
