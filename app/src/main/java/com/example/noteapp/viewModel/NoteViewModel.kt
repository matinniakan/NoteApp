package com.example.noteapp.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.data.repository.NoteRepository
import com.example.noteapp.utils.DataStatus
import com.example.noteapp.utils.EDUCATION
import com.example.noteapp.utils.HEALTH
import com.example.noteapp.utils.HIGH
import com.example.noteapp.utils.HOME
import com.example.noteapp.utils.LOW
import com.example.noteapp.utils.NORMAL
import com.example.noteapp.utils.WORK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) :ViewModel(){
    //spinners
    val categoriesList = MutableLiveData<MutableList<String>>()
    val prioritiesList = MutableLiveData<MutableList<String>>()
    var noteData = MutableLiveData<DataStatus<NoteEntity>>()

    fun loadCategoriesData() = viewModelScope.launch(Dispatchers.IO){
        val data = mutableListOf(WORK, EDUCATION, HOME, HEALTH)
        categoriesList.postValue(data)
    }

    fun loadPrioritiesData() = viewModelScope.launch(Dispatchers.IO){
        val data = mutableListOf(HIGH, NORMAL, LOW)
        prioritiesList.postValue(data)
    }

    fun saveEditNote(isEdit:Boolean,entity: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        if(isEdit){
            repository.updateNote(entity)
        }else{
            repository.saveNote(entity)
        }
    }

    fun getNote(id:Int) =viewModelScope.launch {
        repository.getNote(id).collect{
            noteData.postValue(DataStatus.success(it,false))
        }
    }
}