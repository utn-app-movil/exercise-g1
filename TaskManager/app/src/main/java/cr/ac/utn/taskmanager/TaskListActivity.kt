package cr.ac.utn.taskmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recycler= findViewById<RecyclerView>(R.id.rvTasks_List)
        val taskAdapter = TaskListAdapter(TaskViewModel.getTasks(),
            this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter

        // Componentes de búsqueda y filtrado
        val searchView = findViewById<SearchView>(R.id.searchTasks_List)
        val spinnerStatus = findViewById<Spinner>(R.id.spinnerStatus_List)

        // Aplicar ambos filtros
        fun applyFilters() {

            val selectedStatus = when (spinnerStatus.selectedItemPosition) {
                1 -> TaskStatus.PENDING
                2 -> TaskStatus.IN_PROGRESS
                3 -> TaskStatus.COMPLETED
                4 -> TaskStatus.CANCELLED
                else -> null
            }

            val searchText = searchView.query.toString()

            val filteredTasks = TaskViewModel.filterTasks(
                searchText,
                selectedStatus
            )

            taskAdapter.updateTasks(filteredTasks)
        }
        // Detectar cambios en el texto de búsqueda
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    applyFilters()
                    return true
                }
            }
        )
        // Detectar cambios en el estado seleccionado
        spinnerStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    applyFilters()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    applyFilters()
                }
            }

        // Permitir eliminar tareas mediante deslizamiento
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
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

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {

                // Obtener la posición de la tarea deslizada
                val position = viewHolder.bindingAdapterPosition

                // Obtener la tarea correspondiente
                val task = taskAdapter.getTaskAt(position)

                if (task == null) {
                    applyFilters()
                    return
                }

                // Eliminar la tarea
                TaskViewModel.removeTask(task.id)

                // Actualizar la lista respetando los filtros
                applyFilters()

                // Mostrar mensaje con opción de deshacer
                Snackbar.make(
                    recycler,
                    R.string.TextDeleteSuccess,
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.TextSnackbar) {

                    // Restaurar la tarea eliminada
                    TaskViewModel.saveTask(task)

                    // Actualizar nuevamente la lista
                    applyFilters()

                }.show()
            }
        }

// Asociar el gesto al RecyclerView
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler)
    }

    override fun onItemClick(task: Task) {
        util.openActivity(
            this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID,
            task.id
        )
    }
}