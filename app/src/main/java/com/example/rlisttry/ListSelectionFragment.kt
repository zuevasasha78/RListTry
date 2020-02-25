package com.example.rlisttry

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlisttry.adapter.ListSelectionRecyclerViewAdapter

class ListSelectionFragment : Fragment(),
    ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {

    private var listener: OnListItemFragmentInteractionListener? = null
    lateinit var listRecyclerView: RecyclerView
    lateinit var listDataManager: ListDataManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListItemFragmentInteractionListener) {
            listener = context
            listDataManager = ListDataManager(context as Activity)
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val lists = listDataManager.readLis()
        view?.let {
            listRecyclerView = it.findViewById(R.id.lists_recyclerview)
            listRecyclerView.layoutManager = LinearLayoutManager(context)
            listRecyclerView.adapter =
                ListSelectionRecyclerViewAdapter(lists, this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_selection, container, false)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun listItemClicked(list: TaskList) {
        listener?.onListItemClick(list)
    }

    fun addList(list: TaskList) {
        listDataManager.saveList(list)

        val recyclerAdapter = listRecyclerView.adapter
                as ListSelectionRecyclerViewAdapter
        recyclerAdapter.addList(list)
    }

    fun saveList(list: TaskList) {
        listDataManager.saveList(list)
        updateLists()
    }

    private fun updateLists() {
        val lists = listDataManager.readLis()
        listRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)
    }

    interface OnListItemFragmentInteractionListener {
        fun onListItemClick(list: TaskList)
    }

    companion object {
        fun newInstance(): ListSelectionFragment {
            return ListSelectionFragment()
        }
    }
}
