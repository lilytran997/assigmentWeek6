package com.example.assigmentweek6

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.example.assigmentweek6.ROOM.*
import kotlinx.android.synthetic.main.activity_details_task.*

class DetailsTaskActivity : AppCompatActivity() {

    //val spinnerData = ArrayList<Pair<String, Int>>()
   // var users: ArrayList<User> = ArrayList()
   // lateinit var userDao: UserDAO
  //  lateinit var db: UserDatabase
  //  var userId = "Unassigned"
  //  var userIntentId = -1
  //  var userAssignedName = "unassigned"
    var userPosition = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_task)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = "Task Details"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        getData()

    }

    private fun getData() {

        val data = intent.extras
        if (data!=null){
            val task = data.getParcelable(TASK_ITEM_KEY) as Task
            userPosition = data.getInt(TASK_POSITION_KEY)
            show_title.text = task.description
            check.isChecked = task.completed
            user_task.text = task.userid
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
}
