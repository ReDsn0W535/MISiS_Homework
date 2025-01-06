package com.todotodo.todoitem

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.todotodo.todoitemsviewmodel.TodoItemsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.surface
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.surfaceContainerHigh
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Default.Done,
            tint =  if (dismissState.dismissDirection== SwipeToDismissBoxValue.StartToEnd) MaterialTheme.colorScheme.onSurface else Color.Transparent,
            contentDescription = "Done"
        )
        Icon(
            Icons.Default.Delete,
            tint = if (dismissState.dismissDirection== SwipeToDismissBoxValue.EndToStart) MaterialTheme.colorScheme.onSurface else Color.Transparent,
            contentDescription = "delete"
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItem(item: TodoItemData, viewModel : TodoItemsViewModel, selectFunc:((TodoItemData) -> Unit))
{
    val dateFormat = remember{ SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()) }
    val done = remember { mutableStateOf(false) }
    LaunchedEffect(item.isCompleted) {done.value= item.isCompleted }
    val visible = remember { MutableTransitionState(true) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    if(!done.value){
                        done.value=true
                        viewModel.addDoneTask()
                    }
                    visible.targetState=false
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    if(done.value){
                        done.value=false
                        viewModel.subtractDoneTask()
                    }
                    visible.targetState=false
                }
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * .25f }
    )

    AnimatedVisibility(visible)
    {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = { DismissBackground(dismissState) },
            modifier = Modifier
                .fillMaxWidth()
        ){
            Card(
                modifier = Modifier.fillMaxSize().background(shape = RectangleShape, color = MaterialTheme.colorScheme.primary),
                colors = CardColors(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.onTertiary
                ),
            ) {
                HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outline)
                Row(verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Start,modifier = Modifier
                        .fillMaxWidth()
                        .weight(11f)) {
                        Checkbox(
                            checked = item.isCompleted,
                            onCheckedChange = {
                                if(it!=item.isCompleted){
                                    if(it){
                                        viewModel.addDoneTask()
                                    }
                                    else{
                                        viewModel.subtractDoneTask()
                                    }
                                }
                                viewModel.setItem(item.copy(isCompleted = it));
                            },
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = MaterialTheme.colorScheme.onSurface,
                                checkedColor = MaterialTheme.colorScheme.surfaceBright,
                                uncheckedColor = getPriorityColor(item.priority),
                            ),


                            )
                        if (!item.isCompleted && item.priority == TodoPriority.HIGH) {
                            Icon(Icons.Sharp.KeyboardArrowUp,"HighPriority",tint = getPriorityColor(
                                TodoPriority.HIGH), modifier = Modifier.absoluteOffset(x= (-6).dp))
                        }
                        if (!item.isCompleted && item.priority == TodoPriority.LOW) {
                            Icon(Icons.Sharp.KeyboardArrowDown,"LowPriority",tint = getPriorityColor(
                                TodoPriority.LOW)
                            )
                        }
                        if (item.isCompleted || item.priority == TodoPriority.NORMAL) {
                            Icon(Icons.Sharp.KeyboardArrowDown,"LowPriority",tint = MaterialTheme.colorScheme.primary)
                        }
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = item.text,
                                textAlign = TextAlign.Justify,
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (item.deadLine != null) {
                                Text(
                                    text = dateFormat.format(item.deadLine!!),
                                    textAlign = TextAlign.Left,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = {selectFunc(item)},modifier= Modifier
                            .background(color = Color(0))
                            .weight(1f)
                            .padding(end = 3.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription="Get item info",
                            tint = MaterialTheme.colorScheme.surfaceContainerLow)
                    }


                }

            }
        }
        DisposableEffect(true) {
            onDispose {
                if(!visible.targetState){
                    Log.d("TodoItem", "deleting item")
                    viewModel.deleteTodoItem(item.id);
                }
            }
        }
    }
}