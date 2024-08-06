package com.github.ebrahimi16153.noteapp.utils

data class NoteWrapper<out T>(val status: Status, val data:T? = null, val isEmpty:Boolean){





    enum class Status{
        SUCCESS,
    }

    companion object{
        fun <T> success(data:T?, isEmpty: Boolean):NoteWrapper<T> = NoteWrapper(Status.SUCCESS,data,isEmpty)
    }

}