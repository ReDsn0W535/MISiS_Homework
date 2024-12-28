package com.todo.todoCard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.todo.components.CustomCheckbox
import com.todo.todoItem.Priorities
//import com.todo.Priorities
import com.todo.todoItem.TodoItem
import java.text.SimpleDateFormat

@Composable
fun ToDoItemContent(
    item: TodoItem,
    modifier: Modifier = Modifier,
    onComplete: (Boolean) -> Unit = {},
    onEdit: (TodoItem) -> Unit = {}
) {
    var textColor = when {
        item.isCompleted -> MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.onPrimary
    }

    var iconColor = when {
        item.isCompleted -> MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.onSecondary
    }

    val checkboxBorderColor = when {
        item.isCompleted -> Color.Transparent
        item.importance == Priorities.HIGH -> MaterialTheme.colorScheme.surfaceContainerHigh
        else -> MaterialTheme.colorScheme.outlineVariant
    }


    val lowArrow = when (item.importance) {
        Priorities.LOW -> true
        else -> false
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomCheckbox(
            checked = item.isCompleted,
            onCheckedChange = { onComplete(it) },
            modifier = Modifier
                .size(24.dp)
                .padding()
                .border(2.dp, checkboxBorderColor, CircleShape)
        )

        if (lowArrow) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Low pr",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(-90f)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.text,
                textAlign = TextAlign.Left,
                color = textColor,
                style = if (item.isCompleted) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else {
                    TextStyle()
                },
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            item.deadline?.let { deadline ->
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Срок выполнения",
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = SimpleDateFormat("yyyy.MM.dd").format(deadline),
                        color = textColor
                    )
                }
            }

        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Swipe indicator",
            tint = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.size(24.dp)
        )
    }

}