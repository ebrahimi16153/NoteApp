package com.github.ebrahimi16153.noteapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ebrahimi16153.noteapp.data.model.NoteModel
import com.github.ebrahimi16153.noteapp.data.repository.MainRepository
import com.github.ebrahimi16153.noteapp.data.repository.NoteRepository
import com.github.ebrahimi16153.noteapp.utils.Constant
import com.github.ebrahimi16153.noteapp.utils.NoteWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {


    val notes = MutableLiveData<NoteWrapper<MutableList<NoteModel>>>()


    fun getAllNotes() = viewModelScope.launch(Dispatchers.Main) {

        repository.getNotes().collectLatest {
            notes.postValue(NoteWrapper.success(data = it,it.isEmpty()))
        }
    }

    fun deleteNote(noteModel: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(noteModel)

    }


    fun searchNote(searchQuery: String) = viewModelScope.launch(Dispatchers.Main) {
        repository.getSearchNote(searchQuery).collectLatest {
           notes.postValue(NoteWrapper.success(data = it,it.isEmpty()))
        }
    }

    fun filterNoteByPriority(priority: String) = viewModelScope.launch(Dispatchers.Main) {
        repository.filterByPriority(priority = priority).collectLatest {
            notes.postValue(NoteWrapper.success(data = it,it.isEmpty()))
        }

    }

}