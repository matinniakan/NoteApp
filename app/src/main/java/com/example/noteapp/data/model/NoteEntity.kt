package com.example.noteapp.data.model

import android.provider.SyncStateContract.Constants
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteapp.utils.NOTE_TABLE

@Entity(tableName = NOTE_TABLE)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int =0,
    var title:String ="",
    var desc:String ="",
    var category:String ="",
    var priority:String ="",
)
