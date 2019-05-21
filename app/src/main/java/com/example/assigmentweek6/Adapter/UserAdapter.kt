package com.example.assigmentweek6.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.assigmentweek6.R
import com.example.assigmentweek6.ROOM.User
import kotlinx.android.synthetic.main.item_user.view.*

@SuppressLint("SetTextI18n")
class UserAdapter(var items:ArrayList<User>, val context: Context): RecyclerView.Adapter<UserAdapter.UserViewHolder>()  {
    lateinit var mListener: ItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.username.text = items[position].name
        userViewHolder.delete_icon.setOnClickListener {
            mListener.onDeleteItem(position)
        }
    }

    fun setListenner(listener: ItemClickListener){
        this.mListener = listener
    }
    fun setData(items: ArrayList<User>) {
        this.items = items
    }

    fun appendData(newUserAdded: User) {
        this.items.add(newUserAdded)
        notifyItemInserted(items.size - 1)
    }


    class UserViewHolder ( itemView: View): RecyclerView.ViewHolder(itemView) {
       val username = itemView.username
        val delete_icon = itemView.delete

    }
}