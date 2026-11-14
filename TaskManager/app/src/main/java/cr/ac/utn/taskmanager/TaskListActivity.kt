package cr.ac.utn.taskmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var taskList: MutableList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)


        taskList = TaskViewModel.getTasks().toMutableList()
        taskAdapter = TaskListAdapter(taskList, this)

        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.adapter = taskAdapter

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition


                val deletedTask = taskAdapter.removeItem(position)
                TaskViewModel.removeTask(deletedTask.id)

                Snackbar.make(recycler, "Tarea eliminada", Snackbar.LENGTH_LONG)
                    .setAction("DESHACER") {

                        TaskViewModel.saveTask(deletedTask)
                        taskAdapter.restoreItem(deletedTask, position)
                        recycler.scrollToPosition(position)
                    }
                    .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)
    }

    override fun onItemClick(task: Task) {
        util.openActivity(
            this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id
        )
    }
}