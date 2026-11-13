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
    private lateinit var searchView: SearchView
    private lateinit var statusSpinner: Spinner

    private val statuses = listOf<TaskStatus?>(null) + TaskStatus.entries

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)
        taskAdapter = TaskListAdapter(emptyList(), this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = taskAdapter

        searchView = findViewById(R.id.svTasks_List)
        statusSpinner = findViewById(R.id.spStatus_List)

        setupFilters()
        setupSwipe(recycler)
    }

    override fun onResume() {
        super.onResume()
        refreshTasks()
    }

    private fun setupFilters() {
        val statusLabels = statuses.map { it?.name ?: getString(R.string.TextAllStatuses) }
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusLabels)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = statusAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                refreshTasks()
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return true
            }
        })

        statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                refreshTasks()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                refreshTasks()
            }
        }
    }

    private fun refreshTasks() {
        val query = searchView.query.toString()
        val status = statuses.getOrNull(statusSpinner.selectedItemPosition)
        taskAdapter.updateTasks(TaskViewModel.filterTasks(query, status))
    }

    private fun setupSwipe(recycler: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return

                val task = taskAdapter.getTask(position)
                TaskViewModel.removeTask(task.id)
                refreshTasks()

                Snackbar.make(recycler, R.string.TextDeleteSuccess, Snackbar.LENGTH_LONG)
                    .setAction(R.string.TextSnackbar) {
                        if (TaskViewModel.getTaskById(task.id) == null) {
                            TaskViewModel.saveTask(task)
                        }
                        refreshTasks()
                    }
                    .show()
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(recycler)
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this, TaskActivity::class.java, EXTRA_MESSAGE_ID, task.id)
    }
}