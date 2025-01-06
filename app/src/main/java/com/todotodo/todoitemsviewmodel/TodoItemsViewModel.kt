package com.todotodo.todoitemsviewmodel

import android.util.Log
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.todotodo.tododatapreferences.TodoItemPreferences
import com.todotodo.todoitem.TodoItemData
import com.todotodo.todoitem.TodoPriority
import com.todotodo.todoitem.updateDate
import java.util.Calendar
import java.util.Date
import java.util.UUID.randomUUID

class TodoItemsViewModel : ViewModel(){
    private val _itemsData : MutableList<TodoItemData> = mutableStateListOf();
    private val _doneTasksCount : MutableIntState = mutableIntStateOf(0);
    private var _isDarkTheme : MutableState<Boolean> = mutableStateOf(false);

    init{
        if(TodoItemPreferences.hasStartingItems()){
            _itemsData.addAll(TodoItemPreferences.readItems());
            _doneTasksCount.intValue= TodoItemPreferences.getDoneItemsCount()
            _isDarkTheme.value = TodoItemPreferences.isDarkTheme()
        }
        else{
            Log.d("ViewModel.init", "initialize items",)
            val calendar = Calendar.getInstance()

            val initialItems = listOf(
                TodoItemData(
                    id = "0",
                    text = "Купить продукты для ужина",
                    priority = TodoPriority.HIGH,
                    deadLine = calendar.apply { set(2024, Calendar.DECEMBER, 15) }.time,
                    isCompleted = true,
                    createdAt = Date(),
                    modifiedAt = Date(),
                ),
                TodoItemData(
                    id = "1",
                    text = "Позвонить другу и обсудить планы на отпуск. Длинный текст для проверки того, как это будет отображаться, если он займет больше трёх строк. Текст Текст Текст Текст Текст Текст",
                    priority = TodoPriority.HIGH,
                    deadLine = null,
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null,
                ),
                TodoItemData(
                    id = "2",
                    text = "Начальные дела для дебага",
                    priority = TodoPriority.LOW,
                    deadLine = calendar.apply { set(2024, Calendar.NOVEMBER, 30) }.time,
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null
                )
            )
            for( i in 1..10){
                initialItems.forEach{item ->
                    val itemCopy=item.copy(id=getFreeId());
                    addItemBack(itemCopy)
                };
            }
            TodoItemPreferences.stopStartingItemsSetup(10);
            _doneTasksCount.intValue=10;
            _itemsData.forEach({Log.d("Items", it.id)})
        }
    }

    public fun getDoneTasksCount(): Int{
        return _doneTasksCount.intValue;
    }
    public fun setDoneTasksCount(count : Int){
        _doneTasksCount.intValue=count;
        TodoItemPreferences.setDoneItemsCount(count);
    }
    public fun addDoneTask(){
        setDoneTasksCount(_doneTasksCount.intValue+1);
    }
    public fun subtractDoneTask(){
        setDoneTasksCount(_doneTasksCount.intValue-1);
    }

    public fun isDarkTheme(): Boolean{
        return _isDarkTheme.value;
    }
    public fun setTheme(isDarkTheme : Boolean){
        _isDarkTheme.value=isDarkTheme;
        TodoItemPreferences.setTheme(isDarkTheme);
    }

    public fun getItems() : List<TodoItemData>{
        return _itemsData;
    }
    public fun getFreeId() : String {
        var result = randomUUID().toString();
        while(_itemsData.any { item -> item.id == result}){
            result = randomUUID().toString();
        }
        return result;
    }
    public fun addItem(item : TodoItemData, index : Int){
        val updatedItem = updateDate(item)
        Log.d("addItem", "${index}, ${_itemsData.size}, ${updatedItem}")
        if(index>_itemsData.size || index<0){
            return;
        }
        val hasId : Boolean = _itemsData.any{it.id == updatedItem.id;};
        if(hasId){
            return;
        }
        if(index!=_itemsData.size){
            updatedItem.nextId=_itemsData[index].id;
        }
        _itemsData.add(index,updatedItem);
        when (index){
            0 -> {
                TodoItemPreferences.writeHead(updatedItem);
            }
            1 -> {
                _itemsData.first().nextId=updatedItem.id;
                TodoItemPreferences.writeHead(_itemsData.first());
                TodoItemPreferences.writeItem(updatedItem);
            }
            else -> {
                _itemsData[index-1].nextId=updatedItem.id;
                TodoItemPreferences.writeItem(_itemsData[index-1]);
                TodoItemPreferences.writeItem(updatedItem);
            }
        }
    }
    public fun addItemBack(item : TodoItemData) {
        addItem(item,_itemsData.size);
    }

    public fun getIndex(itemId : String) : Int{
        var itemIndex : Int =-1;
        _itemsData.forEachIndexed { index, toDoItemData ->
            if(toDoItemData.id==itemId){
                itemIndex=index;
            }
        }
        return itemIndex;
    }
    public fun setItem(item : TodoItemData) {
        val itemIndex : Int =getIndex(item.id);
        val updatedItem = updateDate(item)
        if(itemIndex==-1){
            addItemBack(updatedItem);
            return;
        }
        if(_itemsData[itemIndex]==item){
            return;
        }
        updatedItem.nextId=_itemsData[itemIndex].nextId;
        _itemsData[itemIndex] = updatedItem;
        if(itemIndex==0){
            TodoItemPreferences.writeHead(updatedItem);
        }
        else{
            TodoItemPreferences.writeItem(updatedItem);
        }

    }

    public fun deleteTodoItem(itemId: String) {
        val itemIndex : Int =getIndex(itemId);
        if(itemIndex==-1){
            return;
        }
        _itemsData.removeAt(itemIndex);
        when(itemIndex){
            0 -> {
                if(_itemsData.size==itemIndex){
                    TodoItemPreferences.writeHead(null);
                }
                else{
                    TodoItemPreferences.writeHead(_itemsData.first());
                }
            }
            1 -> {
                TodoItemPreferences.removeItem(itemId)
                if(_itemsData.size==itemIndex){
                    _itemsData.first().nextId="";
                }
                else{
                    _itemsData.first().nextId=_itemsData[1].id;

                }
                TodoItemPreferences.writeHead(_itemsData.first());
            }
            else -> {
                TodoItemPreferences.removeItem(itemId)
                if(_itemsData.size==itemIndex){
                    _itemsData[itemIndex-1].nextId="";
                }
                else{
                    _itemsData[itemIndex-1].nextId=_itemsData[itemIndex].id;

                }
                TodoItemPreferences.writeItem(_itemsData[itemIndex-1]);
            }
        }

    }


};