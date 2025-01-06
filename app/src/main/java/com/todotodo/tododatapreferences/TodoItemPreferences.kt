package com.todotodo.tododatapreferences

import android.content.SharedPreferences
import android.util.Log
import com.todotodo.todoitem.TodoItemData
import com.todotodo.todoitem.TodoPriority
import com.google.gson.Gson
import java.util.Date


object TodoItemPreferences{
    private var mPreferences : SharedPreferences? = null;
    private var mPrefEditor: SharedPreferences.Editor? = null;
    private val mGson = Gson();
    private const val itemHead : String = "item_head";

    public fun initPreferences(preferences: SharedPreferences){

        if(mPreferences ==null){
            mPreferences = preferences;
            mPrefEditor = preferences.edit();
        }
    }


    public fun hasStartingItems() : Boolean {
        if(mPreferences ==null){
            return false;
        }
        if(!mPreferences!!.getBoolean("items_initialized",false)){
            mPrefEditor!!.clear();
            mPrefEditor!!.apply();
            return false;
        }
        else{
            return true;
        }
    }
    public fun stopStartingItemsSetup(count : Int){
        if(mPreferences ==null){
            return;
        }
        mPrefEditor!!.putBoolean("items_initialized",true);
        mPrefEditor!!.putBoolean("is_dark_theme",false);
        mPrefEditor!!.putInt("done_tasks_count",count);
        mPrefEditor!!.apply();
    }

    public fun getDoneItemsCount() : Int {
        if(mPreferences ==null){
            return -1;
        }
        return mPreferences!!.getInt("done_tasks_count",-1);
    }

    public fun setDoneItemsCount(count : Int) {
        if(mPreferences ==null){
            return;
        }
        mPrefEditor!!.putInt("done_tasks_count",count);
        mPrefEditor!!.apply();
    }

    public fun isDarkTheme() : Boolean{
        if(mPreferences ==null){
            return false;
        }
        return mPreferences!!.getBoolean("is_dark_theme",false);
    }

    public fun setTheme(isDarkTheme : Boolean) {
        if(mPreferences ==null){
            return;
        }
        mPrefEditor!!.putBoolean("is_dark_theme",isDarkTheme);
        mPrefEditor!!.apply();
    }





    public fun writeHead(item : TodoItemData?){
        if(mPreferences ==null){
            return;
        }
        if(item==null){
            mPrefEditor!!.putString(itemHead, null);
            mPrefEditor!!.apply();
        }else{
            val itemString= mGson.toJson(simplify(item));
            mPrefEditor!!.putString(itemHead, itemString);
            mPrefEditor!!.apply();
        }
    }

    public fun writeItem(item : TodoItemData){
        if(mPreferences ==null){
            return;
        }
        val itemString= mGson.toJson(simplify(item));
        mPrefEditor!!.putString(item.id, itemString);
        mPrefEditor!!.apply();
    }


    public fun removeItem(itemId : String){
        if(mPreferences ==null){
            return;
        }
        mPrefEditor!!.putString(itemId, null);
        mPrefEditor!!.apply();
    }

    public fun readItems() : ArrayList<TodoItemData>{
        val result = ArrayList<TodoItemData>();
        if(mPreferences ==null){
            return result;
        }
        Log.d("readItems", "all entries: ${mPreferences!!.all.toString()}");
        val itemsMap= mPreferences!!.all;
        var id = itemHead;
        while(id!=""){
            val itemStr = itemsMap[id] as String? ?: break;
            val item = restoreItem(mGson.fromJson(itemStr, SimplifiedItemData::class.java));
            result.add(item);
            id=item.nextId;
        }
        return result;
    }
}


data class SimplifiedItemData(val id : String ="",val nextId : String,val text : String="", val intData : ArrayList<Long>);

fun simplify(item : TodoItemData) : SimplifiedItemData {
    val result =
        SimplifiedItemData(item.id,item.nextId,item.text, arrayListOf(-1L,-1L,-1L,-1L,-1L));
    when(item.priority){
        TodoPriority.LOW -> result.intData[0]=0;
        TodoPriority.NORMAL -> result.intData[0]=1;
        TodoPriority.HIGH -> result.intData[0]=2;
    }
    result.intData[1]=if (item.isCompleted) 1 else 0;

    val deadline = item.deadLine?.time;
    if(deadline!=null){
        result.intData[2]=deadline;
    }

    result.intData[3]=item.createdAt.time;

    val modifiedAt = item.modifiedAt?.time;
    if(modifiedAt!=null){
        result.intData[4]= modifiedAt;
    }

    return result;
}

fun restoreItem(data : SimplifiedItemData) : TodoItemData {

    val priority =
        when(data.intData[0]){
            0L -> TodoPriority.LOW;
            1L -> TodoPriority.NORMAL;
            2L -> TodoPriority.HIGH;
            else -> TodoPriority.NORMAL;
        }
    val result = TodoItemData(id= data.id, nextId = data.nextId,text=data.text,priority=priority, isCompleted = (data.intData[1] != 0L), createdAt = Date(data.intData[3]));
    if(data.intData[2]!=-1L){
        result.deadLine= Date(data.intData[2]);
    }
    if(data.intData[4]!=-1L){
        result.modifiedAt= Date(data.intData[4]);
    }
    return result;
}

