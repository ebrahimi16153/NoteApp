package com.github.ebrahimi16153.noteapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ebrahimi16153.noteapp.data.model.NoteModel
import com.github.ebrahimi16153.noteapp.data.repository.NoteRepository
import com.github.ebrahimi16153.noteapp.utils.Constant.EDUCATION
import com.github.ebrahimi16153.noteapp.utils.Constant.HEALTH
import com.github.ebrahimi16153.noteapp.utils.Constant.HOME
import com.github.ebrahimi16153.noteapp.utils.Constant.WORK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {

    val categoryList = MutableLiveData<MutableList<String>>()
    val priorityList = MutableLiveData<MutableList<String>>()
    val noteByID = MutableLiveData<NoteModel>()

    // getCategoryData
    fun loadCategoryData() = viewModelScope.launch(Dispatchers.IO) {
        val data = mutableListOf(WORK, EDUCATION, HOME, HEALTH)
        categoryList.postValue(data)
    }

    //get priority data
    fun loadPriorityData() = viewModelScope.launch(Dispatchers.IO) {
        val data = mutableListOf("High", "Medium", "Low")
        priorityList.postValue(data)
    }

    //save note
    fun saveNote(isEdit: Boolean, note: NoteModel) = viewModelScope.launch(Dispatchers.IO) {

        if (isEdit) repository.updateNote(note) else repository.saveNote(note)
    }

    //getNoteByID
    fun getNoteByID(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.getNote(id).collectLatest {
            noteByID.postValue(it)
        }
    }


}