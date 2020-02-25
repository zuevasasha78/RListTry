package com.example.rlisttry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlisttry.activities.MainActivity
import com.example.rlisttry.adapter.ListItemsRecyclerViewAdapter

class ListDetailFragment : Fragment() {

    lateinit var lists: TaskList
    lateinit var listItemRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            lists = it.getParcelable(MainActivity.INTENT_LIST_KEY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_detail, container, false)

        view?.let {
            listItemRecyclerView = it.findViewById(R.id.list_items_reyclerview)
            listItemRecyclerView.adapter = ListItemsRecyclerViewAdapter(lists)
            listItemRecyclerView.layoutManager = LinearLayoutManager(context)
        }
        return view
    }

    fun addTask(item: String) {
        lists.tasks.add(item)

        val listRecyclerAdapter = listItemRecyclerView.adapter
                as ListItemsRecyclerViewAdapter
        listRecyclerAdapter.list = lists
        listRecyclerAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val ARG_LIST = "list"

        fun newInstence(list: TaskList): ListDetailFragment {
            val fragment = ListDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_LIST, list)
            fragment.arguments = args
            return fragment
        }
    }
}
