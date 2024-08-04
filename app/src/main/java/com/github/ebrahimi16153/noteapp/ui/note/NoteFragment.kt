package com.github.ebrahimi16153.noteapp.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.ebrahimi16153.noteapp.databinding.FragmentNoteBinding
import com.github.ebrahimi16153.noteapp.utils.setUpSpinnerByAdapter
import com.github.ebrahimi16153.noteapp.viewmodel.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : BottomSheetDialogFragment() {

    //binding
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding

    //viewModel
    private val viewModel:NoteViewModel by viewModels()

    //category & priority
    private var category = ""
    private var priority = ""

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

//        initViews
        binding?.apply {

            //handel back button
            closeImg.setOnClickListener {
                this@NoteFragment.dismiss()
            }

            //categorySpinner
            viewModel.loadCategoryData()
            viewModel.categoryList.observe(viewLifecycleOwner){
                categoriesSpinner.setUpSpinnerByAdapter(list = it){ item ->
                    category = item
                }
            }

            //prioritySpinner
            viewModel.loadPriorityData()
            viewModel.priorityList.observe(viewLifecycleOwner){
                prioritySpinner.setUpSpinnerByAdapter(list = it){item ->
                    priority = item
                }
            }






        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}