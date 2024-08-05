package com.github.ebrahimi16153.noteapp.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.github.ebrahimi16153.noteapp.data.model.NoteModel
import com.github.ebrahimi16153.noteapp.databinding.FragmentNoteBinding
import com.github.ebrahimi16153.noteapp.utils.Constant
import com.github.ebrahimi16153.noteapp.utils.setUpSpinnerByAdapter
import com.github.ebrahimi16153.noteapp.viewmodel.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : BottomSheetDialogFragment() {

    //binding
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding

    //viewModel
    private val viewModel: NoteViewModel by viewModels()

    //category & priority
    private var category = ""
    private var priority = ""

    //NoteModel
    @Inject
    lateinit var noteEntity: NoteModel

    //noteId
    private var noteId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(layoutInflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getNoteId
        noteId = arguments?.getInt(Constant.ID_KEY) ?: 0

//        initViews
        binding?.apply {

            //handel back button
            closeImg.setOnClickListener {
                this@NoteFragment.dismiss()
            }

            //categorySpinner
            viewModel.loadCategoryData()
            viewModel.categoryList.observe(viewLifecycleOwner) {
                categoriesSpinner.setUpSpinnerByAdapter(list = it) { item ->
                    category = item
                }
            }

            //prioritySpinner
            viewModel.loadPriorityData()
            viewModel.priorityList.observe(viewLifecycleOwner) {
                prioritySpinner.setUpSpinnerByAdapter(list = it) { item ->
                    priority = item
                }
            }
            // if noteId > 0 -> show Note
            if(noteId>0){
                viewModel.getNoteByID(noteId)
                viewModel.noteByID.observe(viewLifecycleOwner){
                    showExistNote(it)
                }
            }
            // Upsert Note
            upsertNote()


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun showExistNote(noteModel: NoteModel){
        binding?.apply {
            titleEdt.setText(noteModel.title)
            descEdt.setText(noteModel.description)
            viewModel.categoryList.value?.let {  categoriesSpinner.setSelection(it.indexOf(noteModel.category)) }
            viewModel.priorityList.value?.let {  prioritySpinner.setSelection(it.indexOf(noteModel.priority)) }
        }
    }

    //save note
    private fun upsertNote() {
        binding?.apply {

            //get data from editTexts
            saveNote.setOnClickListener {
                if (titleEdt.text.isNotEmpty() && descEdt.text.isNotEmpty()) {

                    //fill noteEntity
                    noteEntity.id = noteId
                    noteEntity.title = titleEdt.text.toString()
                    noteEntity.description = descEdt.text.toString()
                    noteEntity.category = category
                    noteEntity.priority = priority
                    if (noteEntity.id == 0) {


                        //save note
                        Toast.makeText(context, "Note Saved Successfully", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.saveNote(isEdit = false, noteEntity)
                        this@NoteFragment.dismiss()

                    } else {

                        //update note
                        Toast.makeText(context, "Note Updated Successfully", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.saveNote(isEdit = true, noteEntity)
                        this@NoteFragment.dismiss()
                    }


                } else {
                    //show error
                    val rootView = dialog?.window?.decorView
                    Snackbar.make(rootView!!, "Please fill all fields", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}