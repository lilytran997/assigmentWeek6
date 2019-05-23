package com.example.assigmentweek6

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.assigmentweek6.ROOM.*
import kotlinx.android.synthetic.main.activity_details_task.*

class DetailsTaskActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var tasks:ArrayList<Task> = ArrayList()
    lateinit var taskDAO: TaskDAO


    var des: String = "a"
    var task = Task()
    var username: ArrayList<String?> = ArrayList()
    lateinit var userDao: UserDAO
    lateinit var db: UserDatabase
    var userId = "Unassigned"
    var userAssignedName = "unassigned"
    var userPosition = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_task)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = "Task Details"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initTaskDatabase()
        initUserDatabase()
        getTask()
        getData()
        db = UserDatabase.invoke(this)
        setupSpinner()
        btn_save.visibility = View.VISIBLE
        saveData()

    }



    private fun getTask() {
        val tasks = taskDAO.getAll()
        this.tasks.addAll(tasks)
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

    private fun initUserDatabase() {
        val db =
            Room.databaseBuilder(
                applicationContext,
                UserDatabase::class.java, DATABASE_USER_NAME
            ).allowMainThreadQueries()
                .build()
        userDao = db.userDAO()
    }

    private fun getData() {

        val data = intent.extras
        if (data!=null) {
            val taskdb = data.getParcelable(TASK_ITEM_KEY) as Task
            userPosition = data.getInt(TASK_POSITION_KEY)
            check.isChecked = taskdb.completed
            userId = taskdb.userid
            user_task.text = userId
            des = taskdb.description
            show_title.text = des
            check.setOnCheckedChangeListener (CompoundButton.OnCheckedChangeListener { compoundButton, b ->

                task = taskDAO.findByDescription(des)
                task.completed = check.isChecked
                taskDAO.update(task)
            })
        }
    }
    private fun saveData() {
        btn_save.setOnClickListener{
            task = taskDAO.findByDescription(des)
            task.userid = userAssignedName
            taskDAO.update(task)

            val intent = Intent()
            intent.putExtra(TASK_POSITION_KEY, userPosition)
            intent.putExtra(TASK_ITEM_KEY,task)
            intent.putExtra(TASK_DELETE_KEY, 0)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.delete,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId){
            R.id.delete_title ->{
                val builder = AlertDialog.Builder(this@DetailsTaskActivity)
                builder.setTitle("Confirmation")
                    .setMessage("Are you sure to remove this task?")
                    .setPositiveButton("OK") { _, _ ->
                        val intent = Intent()
                        intent.putExtra(TASK_POSITION_KEY, userPosition)
                        intent.putExtra(TASK_DELETE_KEY, 1)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialog, _ -> dialog?.dismiss() }

                val myDialog = builder.create()
                myDialog.show()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return false
    }

    private fun setupSpinner() {
        val users: List<User> = userDao.getAll()
        username.add("Assign Task")
        for (i in users) {
            username.add(i.name)
        }
        username.add("Unassign")

        var spinner: Spinner? = null
        spinner = this.assign_task
        spinner!!.setOnItemSelectedListener(this)


        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, username)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        user_task.text = userId
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        userAssignedName = parent?.selectedItem.toString()
    }


}
