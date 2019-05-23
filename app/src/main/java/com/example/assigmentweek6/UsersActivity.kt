package com.example.assigmentweek6

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.assigmentweek6.Adapter.ItemClickListener
import com.example.assigmentweek6.Adapter.UserAdapter
import com.example.assigmentweek6.ROOM.*
import kotlinx.android.synthetic.main.activity_users.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class UsersActivity : AppCompatActivity() {
    var users: ArrayList<User> = ArrayList()
    lateinit var userAdapter: UserAdapter
    lateinit var userDao: UserDAO
    lateinit var db: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = "Users"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        user_list.layoutManager = LinearLayoutManager(this@UsersActivity) as RecyclerView.LayoutManager
        userAdapter = UserAdapter(users,this@UsersActivity)
        user_list.adapter = userAdapter
        userAdapter.setListenner(itemClick)

        initUserDatabase()
        getUsers()
        db = UserDatabase.invoke(this)

        add_user.setOnClickListener {
            val usernew = User()
            usernew.name = txt_user.text.toString()
            val userDao = db.userDAO()
            val id = userDao.insert(usernew)
            usernew.id = id.toInt()
            userAdapter.appendData(usernew)
        }

    }

    private fun getUsers() {
        val Users = userDao.getAll()
        this.users.addAll((Users))
        userAdapter.notifyDataSetChanged()
    }

    private fun initUserDatabase() {

        val db =
                Room.databaseBuilder(
                    applicationContext,
                    UserDatabase::class.java, DATABASE_USER_NAME
                ).allowMainThreadQueries()
                    .build()
        userDao = db.userDAO()
    }

    private val itemClick = object : ItemClickListener {
        override fun onItemCLicked(position: Int) {

        }

        override fun onDeleteItem(position: Int) {

            val builder = AlertDialog.Builder(this@UsersActivity)
            builder.setTitle("Confirmation")
                .setMessage("Are you sure to remove this user?")
                .setPositiveButton("OK"){_,_->
                    removeItem(position)
                }
                .setNegativeButton(
                    "Cancel"
                ){dialog, _ -> dialog?.dismiss()  }
            val myDialog = builder.create()
            myDialog.show()
        }

    }

    private fun removeItem(position: Int) {
        userDao.delete(users[position])
        users.removeAt(position)
        userAdapter.notifyItemRemoved(position)
        Timer(false).schedule(500) {
            runOnUiThread {
                userAdapter.setData(users)
                userAdapter.notifyDataSetChanged()
            }
        }
    }

}
