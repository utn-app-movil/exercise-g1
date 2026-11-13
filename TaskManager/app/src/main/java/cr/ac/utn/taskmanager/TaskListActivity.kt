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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var recycler: RecyclerView
    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var searchView: SearchView
    private lateinit var spinnerStatus: Spinner

    private var allTasks = listOf<Task>()
    private var currentQuery: String? = null
    private var currentStatus: TaskStatus? = null

    private val statusOptions = listOf(
        Pair("TODAS", null),
        Pair("PENDING", TaskStatus.PENDING),
        Pair("IN_PROGRESS", TaskStatus.IN_PROGRESS),
        Pair("COMPLETED", TaskStatus.COMPLETED),
        Pair("CANCELLED", TaskStatus.CANCELLED)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recycler = findViewById(R.id.rvTasks_List)
        searchView = findViewById(R.id.searchView_Tasks)
        spinnerStatus = findViewById(R.id.spinnerStatus_Tasks)

        allTasks = TaskViewModel.getTasks()
        taskAdapter = TaskListAdapter(allTasks, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter

        setupSpinner()
        setupSearch()
    }

    override fun onResume() {
        super.onResume()
        allTasks = TaskViewModel.getTasks()
        filterTasks(currentQuery, currentStatus)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusOptions.map { it.first }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter

        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentStatus = statusOptions[position].second
                filterTasks(currentQuery, currentStatus)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                currentStatus = null
                filterTasks(currentQuery, currentStatus)
            }
        }
    }

    private fun setupSearch() {
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
    }

    private fun filterTasks(query: String?, status: TaskStatus?) {
        val filtered = allTasks.filter { task ->
            val matchesQuery = if (query.isNullOrBlank()) {
                true
            } else {
                task.title.contains(query, ignoreCase = true) ||
                task.description.contains(query, ignoreCase = true) ||
                task.assignedTo.contains(query, ignoreCase = true)
            }

            val matchesStatus = if (status == null) {
                true
            } else {
                task.status == status
            }

            matchesQuery && matchesStatus
        }
        taskAdapter.updateData(filtered)
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }
}
