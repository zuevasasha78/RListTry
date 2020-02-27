package com.example.rlisttry.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rlisttry.ListDetailFragment
import com.example.rlisttry.ListSelectionFragment
import com.example.rlisttry.R
import com.example.rlisttry.TaskList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    ListSelectionFragment.OnListItemFragmentInteractionListener {

    private var largeScreen = false
    private var listFragment: ListDetailFragment? = null
    private var fragmentContainer: FrameLayout? = null
    private var listSelectionFragment: ListSelectionFragment =
        ListSelectionFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listSelectionFragment =
            supportFragmentManager.findFragmentById(R.id.list_selection_fragment) as
                    ListSelectionFragment
        fragmentContainer = findViewById(R.id.fragment_container)

        largeScreen = fragmentContainer != null

        fab.setOnClickListener {
            showCreateListDiolog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LIST_DETAIL_REQUEST_CODE) {
            data?.let {
                listSelectionFragment.saveList(data.getParcelableExtra(INTENT_LIST_KEY))
            }
        }
    }

    override fun onListItemClick(list: TaskList) {
        showListDetail(list)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        title = resources.getString(R.string.app_name)
        listFragment?.lists?.let {
            listSelectionFragment.listDataManager.saveList(it)
        }
        listFragment?.let {
            supportFragmentManager
                .beginTransaction()
                .remove(it)
                .commit()
            listFragment = null
        }

        fab.setOnClickListener {
            showCreateListDiolog()
        }
    }

    fun showCreateListDiolog() {
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            val list = TaskList(listTitleEditText.text.toString())
            listSelectionFragment.addList(list)
            dialog.dismiss()
            showListDetail(list)
        }
        builder.create().show()
    }

    fun showListDetail(list: TaskList) {
        if (!largeScreen) {
            val listDetailIntent = Intent(this, ListDetailActivity::class.java)
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)

            startActivityForResult(listDetailIntent, LIST_DETAIL_REQUEST_CODE)
        } else {
            title = list.name

            listFragment = ListDetailFragment.newInstence(list)
            listFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it, getString(R.string.list_fragment_tag))
                    .addToBackStack(null)
                    .commit()
            }

            fab.setOnClickListener {
                showCreateTaskDialog()
            }
        }
    }

    fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                listFragment?.addTask(task)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }
}
