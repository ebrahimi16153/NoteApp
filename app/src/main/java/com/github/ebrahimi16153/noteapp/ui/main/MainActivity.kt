package com.github.ebrahimi16153.noteapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.ebrahimi16153.noteapp.databinding.ActivityMainBinding
import com.github.ebrahimi16153.noteapp.ui.note.NoteFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        //init views
        binding?.apply {

            //show Fragment As BottomSheet
            fab.setOnClickListener{
                NoteFragment().show(supportFragmentManager,NoteFragment().tag)
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null


    }
}