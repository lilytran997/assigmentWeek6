package com.example.assigmentweek6

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.assigmentweek6.Adapter.ItemClickListener
import com.example.assigmentweek6.Adapter.TaskAdapter
import com.example.assigmentweek6.ROOM.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    var tasks: ArrayList<Task> = ArrayList()
    lateinit var taskAdapter: TaskAdapter
    lateinit var taskDAO: TaskDAO
    lateinit var db: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = "Room"

        task_list.layoutManager = LinearLayoutManager(this@MainActivity) as RecyclerView.LayoutManager?
        taskAdapter = TaskAdapter(tasks,this@MainActivity)
        task_list.adapter = taskAdapter
        taskAdapter.setListenner(itemClick)


        //setup database
        initTaskDatabase()
        db  = TaskDatabase.invoke(this)
        add_task.setOnClickListener{
            val tasknew = Task()
            tasknew.description = txt_title.text.toString()
            tasknew.completed = false
            tasknew.userid = "Unassigned"
            val taskDAO = db.taskDAO()
            val id = taskDAO.insert(tasknew)
            tasknew.id = id.toInt()
            taskAdapter.appendData(tasknew)
        }
        getTask()


    }

    private fun getTask() {
        val tasks = taskDAO.getAll()
        this.tasks.addAll(tasks)
        taskAdapter.notifyDataSetChanged()
    }

    private fun initTaskDatabase() {
        val db =
                Room.databaseBuilder(
                    applicationContext,
                    TaskDatabase::class.java, DATABASE_TASK_NAME
                ).allowMainThreadQueries()
                    .build()
        taskDAO = db.taskDAO()
    }

    private val itemClick = object : ItemClickListener{
        override fun onDeleteItem(position: Int) {

        }

        override fun onItemCLicked(position: Int) {

            val intent = Intent(this@MainActivity,DetailsTaskActivity::class.java)
            intent.putExtra(TASK_ITEM_KEY,tasks[position])
            intent.putExtra(TASK_POSITION_KEY, position)
            startActivityForResult(intent,1000)
        }

    }
    private fun updateItem(position:Int, task:Task){
        taskDAO.update(task)
        tasks.set(position,task)
        taskAdapter.notifyItemChanged(position)
        Timer(false).schedule(500) {
            runOnUiThread {
                taskAdapter.setData(tasks)
                taskAdapter.notifyDataSetChanged()
            }
        }
    }
    private fun removeItem(position: Int) {
        taskDAO.delete(tasks[position])
        tasks.removeAt(position)

        taskAdapter.notifyItemRemoved(position)
        Timer(false).schedule(500) {
            runOnUiThread {
                taskAdapter.setData(tasks)
                taskAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_user,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when (item!!.itemId){
            R.id.user_add ->{
                val intent = Intent(this@MainActivity,UsersActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return false
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            val test = data?.extras?.getInt(TASK_DELETE_KEY)
            val position = data?.extras?.getInt(TASK_POSITION_KEY)

            if (test == 0) {
                val task_item = data.extras.getParcelable(TASK_ITEM_KEY) as Task

                if (position != null && task_item.description != null && task_item.completed != null && task_item.userid != null) {
                    updateItem(position, Task(task_item.id, task_item.description, task_item.completed, task_item.userid))
                }
            } else {
                if (position != null)
                    removeItem(position)
            }
        }
    }

}
