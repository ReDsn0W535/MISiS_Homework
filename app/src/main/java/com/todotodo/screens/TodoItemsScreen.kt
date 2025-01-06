package com.todotodo.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.todotodo.todoitem.TodoItem
import com.todotodo.todoitem.TodoItemData
import com.todotodo.todoitemsviewmodel.TodoItemsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(viewModel: TodoItemsViewModel, scrollBehavior : TopAppBarScrollBehavior){
    LargeTopAppBar(
        title = {
            Column{
                Text("Мои дела", maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onPrimary)
                AnimatedVisibility(scrollBehavior.state.collapsedFraction == 0.0f) { Text("Выполнено дел: ${viewModel.getDoneTasksCount()}", maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSecondary) }

            }
        },
        actions = {
            Switch(
                checked = viewModel.isDarkTheme(),
                onCheckedChange = { viewModel.setTheme(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.background,
                    checkedTrackColor = MaterialTheme.colorScheme.background,
                    uncheckedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    checkedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarColors(containerColor = MaterialTheme.colorScheme.background, scrolledContainerColor = MaterialTheme.colorScheme.background, navigationIconContentColor = Color.Transparent, titleContentColor = MaterialTheme.colorScheme.onPrimary, actionIconContentColor = Color.Transparent )
    )
}




@Composable
fun AddToItemButton(selectFunc : ((TodoItemData?) -> Unit)){
    IconButton(
        onClick = {selectFunc(null)},
        modifier= Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape
            )
            .border(
                shape = CircleShape,
                width = 0.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ),
        colors = IconButtonColors(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurface)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription="Add item",
            tint = MaterialTheme.colorScheme.onSurface)
    }
}


@Composable
fun TodoItems(viewModel: TodoItemsViewModel, selectFunc:((TodoItemData?) -> Unit), paddingValues: PaddingValues){
    LazyColumn(modifier= Modifier.padding(paddingValues)) {
        Log.d("TodoItemsScreen", "padding = $paddingValues")
        Log.d("TodoItemsScreen", "items count = ${viewModel.getItems().size}")
        items(viewModel.getItems(), key = {it.id}) { item ->
            TodoItem(item, viewModel,selectFunc)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItemsScreen(viewModel : TodoItemsViewModel, selectFunc:((TodoItemData?) -> Unit)){
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = { MainTopAppBar(viewModel,scrollBehavior) },
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background).fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        content = { TodoItems(viewModel, selectFunc = selectFunc,it) },
        floatingActionButton = { AddToItemButton(selectFunc) },
    )
}



