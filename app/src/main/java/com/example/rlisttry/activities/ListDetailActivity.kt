package com.example.rlisttry.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlisttry.R
import com.example.rlisttry.TaskList
import com.example.rlisttry.adapter.ListItemsRecyclerViewAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListDetailActivity : AppCompatActivity() {

    lateinit var lists: TaskList
    lateinit var listItemRecyclerView: RecyclerView
    lateinit var addTaskButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail)

        lists = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)
        title = lists.name

        listItemRecyclerView = findViewById(R.id.list_items_reyclerview)
        listItemRecyclerView.adapter = ListItemsRecyclerViewAdapter(lists)

        listItemRecyclerView.layoutManager = LinearLayoutManager(this)

        addTaskButton = findViewById(R.id.add_task_button)
        addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, lists)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()

    }

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                lists.tasks.add(task)

                val recyclerAdapter = listItemRecyclerView.adapter as
                        ListItemsRecyclerViewAdapter
                recyclerAdapter.notifyItemInserted(lists.tasks.size - 1)

                dialog.dismiss()
            }
            .create()
            .show()
    }
}
