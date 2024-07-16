package com.example.noteapp.data.repository

import com.example.noteapp.data.database.NoteDao
import com.example.noteapp.data.model.NoteEntity
import javax.inject.Inject

class MainRepository @Inject constructor(private val dao: NoteDao) {
    fun getAllNotes() = dao.getAllNotes()
    fun searchNote(search:String) = dao.searchNote(search)
    fun filterNote(filter:String) = dao.filterNote(filter)
    suspend fun deleteNote(entity: NoteEntity) = dao.deleteNote(entity)
}