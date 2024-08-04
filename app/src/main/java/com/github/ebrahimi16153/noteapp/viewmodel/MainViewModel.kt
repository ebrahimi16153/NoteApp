package com.github.ebrahimi16153.noteapp.viewmodel

import androidx.lifecycle.ViewModel
import com.github.ebrahimi16153.noteapp.data.repository.MainRepository
import com.github.ebrahimi16153.noteapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NoteRepository): ViewModel() {


}