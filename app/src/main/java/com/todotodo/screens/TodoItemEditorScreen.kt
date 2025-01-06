package com.todotodo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.todotodo.todoitem.TodoItemData
import com.todotodo.todoitem.TodoPriority
import com.todotodo.todoitem.getPriorityText
import com.todotodo.todoitemsviewmodel.TodoItemsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItemEditor(item : TodoItemData, viewModel: TodoItemsViewModel, expanded : MutableState<Boolean>) {
    val localItem = remember { mutableStateOf(item) }
    val expandedPriorities = remember { mutableStateOf(false) }
    val backgroundAlpha = animateFloatAsState(if (expanded.value) 0.4f else 0f, label = "alpha")

    val editorShape = remember {
        RoundedCornerShape(
            topStart = 15.dp,
            topEnd = 15.dp,
            bottomStart = 15.dp,
            bottomEnd = 15.dp
        )
    }

    val interactionSource = remember { MutableInteractionSource() }
    if (expanded.value) {
        localItem.value = item.copy()
    }

    val dateFormat = remember{ SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()) }

    if(backgroundAlpha.value!=0f){
        Box(modifier = Modifier.fillMaxSize().clickable(indication = null, interactionSource = interactionSource ){expanded.value=false}.background(color = Color.Black.copy(alpha = backgroundAlpha.value)))
    }
    AnimatedVisibility(expanded.value,
        enter = slideIn{ IntOffset( 0, it.height) },
        exit = slideOut{ IntOffset(0, it.height) }
    )
    {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.9f).background(color = MaterialTheme.colorScheme.primary, shape = editorShape).clip(editorShape).clickable(indication = null, interactionSource = interactionSource ){})
            {
                Row(modifier = Modifier.background(color= MaterialTheme.colorScheme.secondary).fillMaxWidth().heightIn(min = 64.dp), horizontalArrangement = Arrangement.Absolute.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    IconButton(
                        onClick = {expanded.value=false}
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Close,
                            contentDescription = "Exit editor",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                    Spacer(Modifier.width(40.dp))
                    Text(
                        "Дело",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1
                    )
                    Spacer(Modifier.width(0.dp))
                    TextButton(
                        onClick = {expanded.value=false;viewModel.setItem(localItem.value)}
                    ) {
                        Text(
                            "Сохранить",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                OutlinedTextField(
                    value = localItem.value.text,
                    onValueChange = {
                        localItem.value = localItem.value.copy(text = it)
                    },
                    label = { Text(text = "Описание задачи") },
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
                    maxLines = 15,
                    shape=editorShape,
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(bottom = 32.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = expandedPriorities.value,
                    onExpandedChange = { expandedPriorities.value = !expandedPriorities.value },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = getPriorityText(localItem.value.priority),
                        label = { Text(text = "Приоритет задачи") },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedPriorities.value) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(0.95f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.secondary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPriorities.value,
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.primary)
                            .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
                        onDismissRequest = { expandedPriorities.value = false }) {
                        TodoPriority.entries.forEach { priority ->
                            DropdownMenuItem(

                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.primary),
                                text = {
                                    Text(
                                        text = getPriorityText(priority),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                },
                                onClick = {
                                    localItem.value = localItem.value.copy(priority=priority)
                                    expandedPriorities.value = false
                                }
                            )
                        }
                    }
                }
                DeadLinePicker(localItem)

                Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize())
                {
                    val createdAt = if(item.createdAt.time==0L) "Сейчас" else dateFormat.format(item.createdAt)
                    val modifiedAt = if(item.modifiedAt==null) "-" else dateFormat.format(item.modifiedAt!!)
                    HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outline)
                    Text("    Дата создания: $createdAt", color= MaterialTheme.colorScheme.onPrimary)
                    Spacer(Modifier.height(16.dp))
                    Text("    Дата последнего изменения: $modifiedAt", color= MaterialTheme.colorScheme.onPrimary)
                    Spacer(Modifier.height(16.dp))
                }

            }
        }
    }
}