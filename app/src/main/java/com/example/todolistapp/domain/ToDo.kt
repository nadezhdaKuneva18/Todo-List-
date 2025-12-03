package com.example.todolistapp.domain

data class ToDo(
    val id:Int =0,
    val title:String,
    var isDone: Boolean=false
)
