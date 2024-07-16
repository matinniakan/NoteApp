package com.example.noteapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.noteapp.R
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.utils.BUNDLE_ID
import com.example.noteapp.utils.EDIT
import com.example.noteapp.utils.NEW
import com.example.noteapp.utils.getIndexFromList
import com.example.noteapp.utils.setUpListWithAdapter
import com.example.noteapp.viewModel.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : BottomSheetDialogFragment() {
    //binding
    private var _binding: FragmentNoteBinding?= null
    private val binding get() = _binding

    @Inject
    lateinit var entity: NoteEntity

    //other
    private val viewModel: NoteViewModel by viewModels()
    private var category = ""
    private var priority = ""
    private var noteId = 0
    private var type = ""
    private var isEdit = false
    private val categoriesList:MutableList<String> = mutableListOf()
    private val prioritiesList:MutableList<String> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteBinding.inflate(layoutInflater  )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //bundle
        noteId = arguments?.getInt(BUNDLE_ID) ?: 0
        if (noteId >0){
            type= EDIT
            isEdit =true
        }else{
            type= NEW
            isEdit=false
        }
        //initViews
        binding?.apply {
            //close
            closeImg.setOnClickListener { dismiss() }
            //spinner category
            viewModel.loadCategoriesData()
            viewModel.categoriesList.observe(viewLifecycleOwner){
                categoriesList.addAll(it)
                categoriesSpinner.setUpListWithAdapter(it){ item ->
                    category = item
                }
            }
            //spinner priority
            viewModel.loadPrioritiesData()
            viewModel.prioritiesList.observe(viewLifecycleOwner){
                prioritiesList.addAll(it)
                prioritiesSpinner.setUpListWithAdapter(it){ item ->
                    priority = item
                }
            }
            //note data
            if(type == EDIT){
                viewModel.getNote(noteId)
                viewModel.noteData.observe(viewLifecycleOwner){ itData ->
                    itData.data?.let { it ->
                        titleEdt.setText(it.title)
                        descEdt.setText(it.desc)
                        categoriesSpinner.setSelection(categoriesList.getIndexFromList(it.category),false)
                        prioritiesSpinner.setSelection(prioritiesList.getIndexFromList(it.priority),false)
                    }
                }
            }
            //click
            saveNote.setOnClickListener{
                val title = titleEdt.text.toString()
                val desc = descEdt.text.toString()
                entity.id = noteId
                entity.title = title
                entity.desc = desc
                entity.category = category
                entity.priority = priority

                //agar edit bashd k data ra dar qesmt note data grftim
                //agar save bashd k dtat ra dar hmin qesmt click grftim
                if(title.isNotEmpty() && desc.isNotEmpty()){
                    viewModel.saveEditNote(isEdit,entity)
                }

                dismiss()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

}