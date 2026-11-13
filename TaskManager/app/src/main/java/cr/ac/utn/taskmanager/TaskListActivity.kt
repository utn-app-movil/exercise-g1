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
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var recycler: RecyclerView

    private var textoBusqueda: String = ""
    private var estadoSeleccionado: String = "TODAS"

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
        val searchView = findViewById<SearchView>(R.id.svSearch_TaskList)
        val spinner = findViewById<Spinner>(R.id.spinnerStatus_TaskList)


        taskAdapter = TaskListAdapter(emptyList(), this)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.adapter = taskAdapter

        // ==================== SWIPE TO DELETE ====================
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, // No permitimos mover elementos drag & drop
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
                val tareaEliminada = taskAdapter.getTaskAt(position)


                TaskViewModel.removeTask(tareaEliminada.id)

                taskAdapter.removeTaskAt(position)


                Snackbar.make(recycler, getString(R.string.task_deleted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo_action)) {
                        TaskViewModel.saveTask(tareaEliminada)

                        filtrarTareas()
                    }.show()
            }
        }


        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)


        val opcionesEstado = listOf("TODAS", "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesEstado)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                textoBusqueda = newText ?: ""
                filtrarTareas()
                return true
            }
        })


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                estadoSeleccionado = opcionesEstado[position]
                filtrarTareas()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    private fun filtrarTareas() {

        val todasLasTareas = TaskViewModel.getTasks()


        val tareasFiltradas = todasLasTareas.filter { task ->

            val coincideTexto = task.title.contains(textoBusqueda, ignoreCase = true) ||
                    task.assignedTo.contains(textoBusqueda, ignoreCase = true)


            val coincideEstado = if (estadoSeleccionado == "TODAS") {
                true
            } else {
                task.status.name == estadoSeleccionado
            }


            coincideTexto && coincideEstado
        }


        taskAdapter.setFilteredTasks(tareasFiltradas)
    }

    override fun onResume() {
        super.onResume()
        filtrarTareas()
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this, TaskActivity::class.java, EXTRA_MESSAGE_ID, task.id)
    }
}