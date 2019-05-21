package com.example.assigmentweek6.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.assigmentweek6.R
import com.example.assigmentweek6.ROOM.Task
import kotlinx.android.synthetic.main.item_task.view.*
@SuppressLint("SetTextI18n")
class TaskAdapter(var items:ArrayList<Task>, val context: Context): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    lateinit var mListener: ItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        return TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(taskViewHolder: TaskViewHolder, position: Int) {

        taskViewHolder.task_name.text = items[position].description
        taskViewHolder.description.text = "Assigned to: "+ items[position].userid

        taskViewHolder.itemView.setOnClickListener {
            mListener.onItemCLicked(position)
        }

    }
    fun setData(items: ArrayList<Task>) {
        this.items = items
    }

    fun appendData(newTask: Task){
        this.items.add(newTask)
        notifyItemInserted(items.size - 1)
    }
    fun setListenner(listener: ItemClickListener){
        this.mListener = listener
    }

    class TaskViewHolder( itemView: View): RecyclerView.ViewHolder(itemView) {

        var task_name = itemView.task_name
        var description = itemView.unassigned
    }
}