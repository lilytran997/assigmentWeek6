package com.example.assigmentweek6

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.assigmentweek6.Adapter.ItemClickListener
import com.example.assigmentweek6.Adapter.UserAdapter
import com.example.assigmentweek6.ROOM.*
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity() {
    var users: ArrayList<User> = ArrayList()
    lateinit var userAdapter: UserAdapter
    lateinit var userDao: UserDAO
    lateinit var db: UserDatabase

    var tasks: java.util.ArrayList<Task> = java.util.ArrayList()
    lateinit var taskDao: TaskDAO
    lateinit var dbTask: TaskDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
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

    }

    private fun initUserDatabase() {

    }

    private val itemClick = object : ItemClickListener {
        override fun onItemCLicked(position: Int) {

        }

        override fun onDeleteItem(position: Int) {

        }

    }

}
