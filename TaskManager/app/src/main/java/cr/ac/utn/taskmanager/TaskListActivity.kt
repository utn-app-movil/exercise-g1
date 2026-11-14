package cr.ac.utn.taskmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var taskAdapter: TaskListAdapter
    private var currentQuery: String? = null
    private var currentStatus: TaskStatus? = null
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

        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
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

                var isRestored = false
                Snackbar.make(recycler, "Tarea eliminada", Snackbar.LENGTH_LONG)
                    .setAction("DESHACER") {
                        if (!isRestored) {
                            isRestored = true
                            if (TaskViewModel.getTaskById(deletedTask.id) == null) {
                                TaskViewModel.saveTask(deletedTask)
                                taskAdapter.restoreItem(deletedTask, position)
                                recycler.scrollToPosition(position)
                            }
                        }
                    }
                    .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)

        val searchView = findViewById<SearchView>(R.id.searchViewTasks)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                currentQuery = query
                filterTasks(currentQuery, currentStatus)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText
                filterTasks(currentQuery, currentStatus)
                return true
            }
        })

        val spinner = findViewById<Spinner>(R.id.spinnerStatusFilter)
        val statusOptions = arrayOf("TODAS", "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = statusOptions[position]
                currentStatus = if (selected == "TODAS") {
                    null
                } else {
                    TaskStatus.valueOf(selected)
                }
                filterTasks(currentQuery, currentStatus)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                currentStatus = null
                filterTasks(currentQuery, currentStatus)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        filterTasks(currentQuery, currentStatus)
    }

    private fun filterTasks(query: String?, status: TaskStatus?) {
        val allTasks = TaskViewModel.getTasks()
        val filtered = allTasks.filter { task ->
            val matchesQuery = query.isNullOrBlank() ||
                    task.title.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true) ||
                    task.assignedTo.contains(query, ignoreCase = true)
            val matchesStatus = status == null || task.status == status
            matchesQuery && matchesStatus
        }
        taskAdapter.updateTasks(filtered)
    }

    override fun onItemClick(task: Task) {
        util.openActivity(
            this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id
        )
    }
}
