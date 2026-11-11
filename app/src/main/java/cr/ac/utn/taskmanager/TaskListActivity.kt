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
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var spinnerFilter: Spinner
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Configurar RecyclerView y Adaptador
        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)
        taskAdapter = TaskListAdapter(TaskViewModel.getTasks().toMutableList(), this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter

        // 2. Configurar el Spinner de Filtros por Estado
        spinnerFilter = findViewById(R.id.spinnerFilterStatus)
        val filterOptions = arrayOf("TODAS", "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filterOptions)
        spinnerFilter.adapter = spinnerAdapter

        // 3. Configurar el SearchView para búsqueda en tiempo real
        searchView = findViewById(R.id.searchView_tasks)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterTasks(query, spinnerFilter.selectedItem.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTasks(newText, spinnerFilter.selectedItem.toString())
                return true
            }
        })

        // 4. Escucha selección del Spinner
        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedStatus = parent.getItemAtPosition(position).toString()
                filterTasks(searchView.query.toString(), selectedStatus)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // 5. FASE 3: Configuración de Swipe to Delete con ItemTouchHelper y Snackbar
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                if (position == RecyclerView.NO_POSITION) return

                val deletedTask = taskAdapter.getTaskAt(position)


                val currentTasks = TaskViewModel.getTasks()



                val rootView = findViewById<View>(R.id.main)
                Snackbar.make(rootView, "Tarea eliminada", Snackbar.LENGTH_LONG)
                    .setAction("DESHACER") {
                        // Si hace clic en deshacer, recalculamos los filtros para restaurar la visualización
                        filterTasks(searchView.query.toString(), spinnerFilter.selectedItem.toString())
                    }
                    .show()

                filterTasks(searchView.query.toString(), spinnerFilter.selectedItem.toString())
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)
    }


    private fun filterTasks(query: String?, statusFilter: String) {
        val allTasks = TaskViewModel.getTasks()

        val filteredList = allTasks.filter { task ->
            val matchesQuery = if (!query.isNullOrBlank()) {
                task.title.contains(query, ignoreCase = true) ||
                        task.description.contains(query, ignoreCase = true)
            } else {
                true
            }

            val matchesStatus = if (statusFilter != "TODAS") {
                task.status.name.equals(statusFilter, ignoreCase = true)
            } else {
                true
            }

            matchesQuery && matchesStatus
        }


        taskAdapter.updateTasks(filteredList)
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this, TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }
}