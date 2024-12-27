package com.todo.todoCard

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.todo.todoItem.TodoItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.math.roundToInt


@Composable
fun ToDoItem(
    item: TodoItem,
    modifier: Modifier = Modifier,
    onComplete: (Boolean) -> Unit = {},
    onEdit: (TodoItem) -> Unit = {},
    onDelete: (String) -> Unit = {},
    onViewDetails: (TodoItem) -> Unit = {}
) {
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val maxOffset = 300f
    val thresholdDelete = -100f
    val thresholdButtons = 100f
    val thresholdComplete = 300f

    var showInfo = remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .padding(5.dp)
            .background(
                when {
                    offsetX.value < 0 -> MaterialTheme.colorScheme.surfaceContainerHigh
                    offsetX.value > 0 -> MaterialTheme.colorScheme.surface
                    else -> MaterialTheme.colorScheme.background
                }
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showInfo.value = true
                    }
                )
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {

                            when {
                                offsetX.value <= thresholdDelete -> {
                                    onDelete(item.id)
                                    offsetX.animateTo(0f)
                                }

                                offsetX.value >= thresholdComplete -> {
                                    onComplete(true)
                                    offsetX.animateTo(0f)
                                }

                                (offsetX.value > thresholdButtons && offsetX.value < thresholdComplete) -> {
                                    offsetX.animateTo(300f)
                                }

                                else -> {
                                    offsetX.animateTo(0f)
                                }
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            offsetX.snapTo(
                                (offsetX.value + dragAmount).coerceIn(-maxOffset, maxOffset)
                            )
                        }
                    }
                )
            }
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (offsetX.value >= thresholdButtons) {
                IconButton(
                    onClick = { onEdit(item) }

                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp),
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    onClick = { onDelete(item.id) }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(MaterialTheme.colorScheme.primary)
        ) {
            ToDoItemContent(
                item = item,
                modifier = modifier.padding(8.dp),
                onComplete = onComplete,
                onEdit = onEdit
            )
        }
    }
    if (showInfo.value) {
        AlertDialog(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(10.dp)
                ),
            containerColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { showInfo.value = !showInfo.value },
            title = {
                Text(
                    text = "Информация о задаче",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            },
            text = {
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = screenHeight * 0.5f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = item.text,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                    }
                    item.deadline?.let { deadline ->
                        Text(
                            text = "Срок: ${SimpleDateFormat("yyyy.MM.dd").format(deadline)}",
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .padding(top = 16.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfo.value = false }) {
                    Text(
                        text = "Закрыть",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        )
    }
}