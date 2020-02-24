package com.example.rlisttry

import android.app.Activity
import android.content.Context

class ListDataManager(private val activity: Activity) {

    fun saveList(list: TaskList) {
        val sharedPreferences =
            activity.getPreferences(Context.MODE_PRIVATE).edit()

        sharedPreferences.putStringSet(list.name, list.tasks.toHashSet())
        sharedPreferences.apply()
    }

    fun readLis(): ArrayList<TaskList> {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val sharedPreferencesContext = sharedPreferences.all

        val taskLists = ArrayList<TaskList>()

        for (taskList in sharedPreferencesContext) {
            val itemHashSet = ArrayList(taskList.value as HashSet<String>)
            val list = TaskList(taskList.key, itemHashSet)

            taskLists.add(list)
        }
        return taskLists
    }
}