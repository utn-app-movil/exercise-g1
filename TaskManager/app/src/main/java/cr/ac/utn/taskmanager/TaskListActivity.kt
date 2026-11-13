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
    private lateinit var searchView: SearchView
    private lateinit var spStatus: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchView = findViewById(R.id.searchTasks_List)
        spStatus = findViewById(R.id.spStatus_List)
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            listOf("TODAS") + TaskStatus.entries.map { it.name })
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = statusAdapter

        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)
        taskAdapter = TaskListAdapter(TaskViewModel.getTasks().toList(), this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = taskAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTasks()
                return true
            }
        })
        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterTasks()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val swipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return
                val task = taskAdapter.getTask(position)
                TaskViewModel.removeTask(task.id)
                filterTasks()
                Snackbar.make(recycler, "Tarea eliminada", Snackbar.LENGTH_LONG)
                    .setAction("DESHACER") {
                        TaskViewModel.saveTask(task)
                        filterTasks()
                    }.show()
            }
        }
        ItemTouchHelper(swipe).attachToRecyclerView(recycler)
    }

    override fun onResume() {
        super.onResume()
        filterTasks()
    }

    private fun filterTasks() {
        val position = spStatus.selectedItemPosition
        val status = if (position > 0) TaskStatus.entries[position - 1] else null
        taskAdapter.updateTasks(TaskViewModel.filterTasks(searchView.query.toString(), status))
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this, TaskActivity::class.java, EXTRA_MESSAGE_ID, task.id)
    }
}
