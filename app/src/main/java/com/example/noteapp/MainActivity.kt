package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.ui.NoteAdapter
import com.example.noteapp.ui.NoteFragment
import com.example.noteapp.utils.ALL
import com.example.noteapp.utils.BUNDLE_ID
import com.example.noteapp.utils.DELETE
import com.example.noteapp.utils.EDIT
import com.example.noteapp.utils.HIGH
import com.example.noteapp.utils.LOW
import com.example.noteapp.utils.NORMAL
import com.example.noteapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //binding
    private var _binding : ActivityMainBinding?= null
    private val binding get() = _binding

    @Inject
    lateinit var noteAdapter: NoteAdapter

    @Inject
    lateinit var noteEntity: NoteEntity

    //other
    private val viewModel: MainViewModel by viewModels()

    private var selectedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //init views
        binding?.apply {
            //support toolbar
            setSupportActionBar(notesToolbar)
            //note fragment
            addNoteBtn.setOnClickListener {
                NoteFragment().show(supportFragmentManager, NoteFragment().tag)
            }
            //get data
            viewModel.getAllNotes()
            viewModel.notesData.observe(this@MainActivity){
                showEmpty(it.isEmpty)
                noteAdapter.setData(it.data!!)
                noteList.apply {
                    layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    adapter = noteAdapter
                }
            }
            //filter
            notesToolbar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.actionFilter -> {
                        priorityFilter()
                        return@setOnMenuItemClickListener true
                    }
                    else ->{
                        return@setOnMenuItemClickListener false
                    }
                }
            }
            //clicks
            noteAdapter.setOnItemClickListener { entity, type ->
                when(type){
                    EDIT ->{
                        val noteFragment = NoteFragment()
                        val bundle = Bundle()
                        bundle.putInt(BUNDLE_ID,entity.id)
                        noteFragment.arguments =bundle
                        noteFragment.show(supportFragmentManager,noteFragment.tag)
                    }
                    DELETE ->{
                        noteEntity.id = entity.id
                        noteEntity.title = entity.title
                        noteEntity.desc = entity.desc
                        noteEntity.category = entity.category
                        noteEntity.priority = entity.priority
                        viewModel.deleteNote(entity)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        val search = menu.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.getSearchNotes(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun priorityFilter(){
        val builder = AlertDialog.Builder(this)
        val priority = arrayOf(ALL, HIGH, NORMAL, LOW)
        builder.setSingleChoiceItems(priority,selectedItem){ dialog,item ->
            when(item){
                0 ->{
                    viewModel.getAllNotes()
                }
                in 1..3 -> {
                    viewModel.getFilterNotes(priority[item])
                }
            }
            selectedItem = item
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showEmpty(isShown:Boolean){
        binding?.apply {
            if(isShown){
                emptyLay.visibility = View.VISIBLE
                noteList.visibility = View.GONE
            }else{
                emptyLay.visibility = View.GONE
                noteList.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}