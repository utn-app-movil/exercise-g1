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
    private lateinit var svSearch: SearchView
    private lateinit var spStatusFilter: Spinner
    private var searchText: String = ""
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

        svSearch = findViewById(R.id.svSearch_List)
        spStatusFilter = findViewById(R.id.spStatusFilter_List)

        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)
        taskAdapter = TaskListAdapter(TaskViewModel.getTasks(), this)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.adapter = taskAdapter
        setupSwipeToDelete(recycler)

        setupStatusFilter()
        setupSearch()
    }

    override fun onResume() {
        super.onResume()
        applyFilters()
    }

    // ---------- Fase 2: filtro por estado ----------
    private fun setupStatusFilter() {
        val options = listOf("TODAS") + TaskStatus.entries.map { it.name }
        val statusAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, options)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatusFilter.adapter = statusAdapter

        spStatusFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                selectedStatus = if (position == 0) null else TaskStatus.entries[position - 1]
                applyFilters()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // ---------- Fase 2: búsqueda por texto ----------
    private fun setupSearch() {
        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchText = query ?: ""
                applyFilters()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                searchText = newText ?: ""
                applyFilters()
                return true
            }
        })
    }

    private fun applyFilters() {
        val filtered = TaskViewModel.filterTasks(searchText, selectedStatus)
        taskAdapter.updateList(filtered)
    }

    // ---------- Fase 3: deslizar para eliminar con DESHACER ----------
    private fun setupSwipeToDelete(recycler: RecyclerView) {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val deletedTask = taskAdapter.getTaskAt(position)

                TaskViewModel.removeTask(deletedTask.id)
                applyFilters()

                Snackbar.make(findViewById(R.id.main),
                    "Tarea eliminada: ${deletedTask.title}",
                    Snackbar.LENGTH_LONG)
                    .setAction("DESHACER") {
                        TaskViewModel.saveTask(deletedTask)
                        applyFilters()
                    }
                    .show()
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler)
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }
}