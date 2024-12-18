package com.example.noteapp.data.repository

import com.example.noteapp.data.database.NoteDao
import com.example.noteapp.data.model.NoteEntity
import javax.inject.Inject

class NoteRepository @Inject constructor(private val dao:NoteDao) {
    suspend fun saveNote(entity: NoteEntity) = dao.saveNote(entity)
    suspend fun updateNote(entity: NoteEntity) = dao.updateNote(entity)
    fun getNote(id:Int) = dao.getNote(id)
}