package com.example.noteapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.databinding.ItemNotesBinding
import com.example.noteapp.utils.DELETE
import com.example.noteapp.utils.EDIT
import com.example.noteapp.utils.EDUCATION
import com.example.noteapp.utils.HEALTH
import com.example.noteapp.utils.HIGH
import com.example.noteapp.utils.HOME
import com.example.noteapp.utils.LOW
import com.example.noteapp.utils.NORMAL
import com.example.noteapp.utils.WORK
import javax.inject.Inject

class NoteAdapter @Inject constructor() : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private lateinit var binding: ItemNotesBinding
    private lateinit var context: Context
    private var notesList = emptyList<NoteEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //getItem from PagingDataAdapter
        holder.bind(notesList[position])
        //Not duplicate items
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = notesList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: NoteEntity) {
            binding.apply {
                titleTxt.text =item.title
                descTxt.text = item.desc
                //priority
                when(item.priority){
                    HIGH -> priorityColor.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                    NORMAL -> priorityColor.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
                    LOW -> priorityColor.setBackgroundColor(ContextCompat.getColor(context, R.color.aqua))
               }
                //category
                when(item.category){
                    WORK -> categoryImg.setImageResource(R.drawable.work)
                    HOME -> categoryImg.setImageResource(R.drawable.home)
                    EDUCATION -> categoryImg.setImageResource(R.drawable.education)
                    HEALTH -> categoryImg.setImageResource(R.drawable.healthcare)
                }
                //menu
                menuImg.setOnClickListener {
                    val popupMenu =PopupMenu(context,it)
                    popupMenu.menuInflater.inflate(R.menu.menu_items,popupMenu.menu)
                    popupMenu.show()
                    //click
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when(menuItem.itemId){
                            R.id.itemEdit ->
                                onItemClickListener?.let {
                                    it(item, EDIT)
                                }
                            R.id.itemDelete ->
                                onItemClickListener?.let {
                                    it(item, DELETE)
                                }
                        }
                       return@setOnMenuItemClickListener true
                    }
                }

            }
        }
    }

    private var onItemClickListener: ((NoteEntity, String) -> Unit)? = null

    fun setOnItemClickListener(listener: (NoteEntity, String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<NoteEntity>) {
        val notesDiffUtil = NotesDiffUtils(notesList, data)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtil)
        notesList = data
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<NoteEntity>, private val newItem: List<NoteEntity>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }
}