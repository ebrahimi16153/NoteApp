package com.github.ebrahimi16153.noteapp.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.ebrahimi16153.noteapp.R
import com.github.ebrahimi16153.noteapp.data.adapters.NoteAdapter
import com.github.ebrahimi16153.noteapp.data.model.NoteModel
import com.github.ebrahimi16153.noteapp.databinding.ActivityMainBinding
import com.github.ebrahimi16153.noteapp.ui.note.NoteFragment
import com.github.ebrahimi16153.noteapp.utils.Constant
import com.github.ebrahimi16153.noteapp.utils.Constant.ALL
import com.github.ebrahimi16153.noteapp.utils.Constant.HIGH
import com.github.ebrahimi16153.noteapp.utils.Constant.LOW
import com.github.ebrahimi16153.noteapp.utils.Constant.MEDIUM
import com.github.ebrahimi16153.noteapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    //adapter
    @Inject
    lateinit var noteAdapter: NoteAdapter

    //viewModel
    private val viewModel: MainViewModel by viewModels()

    //selected priority
    private var selectedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        //init views
        binding?.apply {

            //supportActionBar
            setSupportActionBar(notesToolbar)


            //show Fragment As BottomSheet
            fab.setOnClickListener {
                NoteFragment().show(supportFragmentManager, NoteFragment().tag)
            }

            //show all notes
            viewModel.getAllNotes()
            viewModel.notes.observe(this@MainActivity) {
                showAllNotes(it)
            }
//            click on filter
            notesToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.main_menu_Filter -> {
                        filterDialogByPriority { priority ->
                            if (priority != ALL) {
                                viewModel.filterNoteByPriority(priority)
                            } else {
                                viewModel.getAllNotes()
                            }
                        }

                        return@setOnMenuItemClickListener true
                    }
                    else -> { return@setOnMenuItemClickListener false }
                }
            }


        }

    }

    private fun showAllNotes(list: MutableList<NoteModel>) {
        Log.i("TAG", "TAG")

        binding?.apply {
            if (list.isNotEmpty()) {
                emptyLay.visibility = View.INVISIBLE
                noteList.visibility = View.VISIBLE
                noteAdapter.setData(list)
                noteList.adapter = noteAdapter
                noteList.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            } else {
                binding?.apply {
                    emptyLay.visibility = View.VISIBLE
                    noteList.visibility = View.INVISIBLE
                }
            }

            //onClickListener

            noteAdapter.seOnItemClickListener { noteModel, s ->
                when (s) {
                    Constant.DELETE -> {
                        viewModel.deleteNote(noteModel)
                    }

                    Constant.EDIT -> {
// send noteID as bundle to NoteFragment
                        val bundle = Bundle()
                        val noteFragment = NoteFragment()
                        bundle.putInt(Constant.ID_KEY, noteModel.id)
                        noteFragment.arguments = bundle
                        noteFragment.show(supportFragmentManager, noteFragment.tag)
                    }
                }
            }

        }

    }

    private fun filterDialogByPriority(priority: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.select_priority))
        val listOfPriority = arrayOf(ALL, HIGH, MEDIUM, LOW)
        builder.setSingleChoiceItems(listOfPriority, selectedItem) { dialog, item ->
            when (item) {
                0 -> { priority(ALL) }

                1 -> { priority(HIGH) }

                2 -> { priority(MEDIUM) }

                3 -> { priority(LOW) }
            }
            selectedItem = item
            dialog.dismiss()

        }
        val dialog:AlertDialog = builder.create()
        dialog.show()
        //change background dialog
        dialog.window?.setBackgroundDrawableResource(R.color.background)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // menu and searchBar
        menuInflater.inflate(R.menu.main_menu, menu)
        val search = menu?.findItem(R.id.main_menu_search)
        val searchView = search?.actionView as SearchView
        // hint
        searchView.queryHint = "Search..."
        // on change Text
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchNote(newText) }
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null


    }
}