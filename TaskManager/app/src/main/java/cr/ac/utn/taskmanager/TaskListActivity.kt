package cr.ac.utn.taskmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var recycler: RecyclerView
    private var searchQuery = ""
    private var selectedStatus: TaskStatus? = null

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
        taskAdapter = TaskListAdapter(emptyList(), this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = taskAdapter

        val searchView = findViewById<SearchView>(R.id.searchTasks_List)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query.orEmpty()
                applyFilters()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText.orEmpty()
                applyFilters()
                return true
            }
        })

        val spinner = findViewById<Spinner>(R.id.spinnerStatus_List)
        val statuses = listOf(getString(R.string.FilterAll)) + TaskStatus.entries.map { it.name }
        val spinnerAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, statuses)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                selectedStatus = if (position == 0) null else TaskStatus.entries[position - 1]
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedStatus = null
                applyFilters()
            }
        }

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    applyFilters()
                    return
                }
                val deletedTask = taskAdapter.getTaskAt(position)
                TaskViewModel.removeTask(deletedTask.id)
                applyFilters()
                Snackbar.make(recycler, R.string.TextDeleteSuccess, Snackbar.LENGTH_LONG)
                    .setAction(R.string.TextUndo) {
                        TaskViewModel.saveTask(deletedTask)
                        applyFilters()
                    }.show()
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler)
        applyFilters()
    }

    override fun onResume() {
        super.onResume()
        if (::taskAdapter.isInitialized) applyFilters()
    }

    private fun applyFilters() {
        taskAdapter.updateTasks(TaskViewModel.filterTasks(searchQuery, selectedStatus))
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this, TaskActivity::class.java, EXTRA_MESSAGE_ID, task.id)
    }
}
