package com.github.ebrahimi16153.noteapp.viewmodel

import androidx.lifecycle.ViewModel
import com.github.ebrahimi16153.noteapp.data.repository.MainRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class MainViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {


}