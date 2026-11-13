package cr.ac.utn.taskmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var etSearch: EditText
    private lateinit var spinnerFilter: Spinner
    private lateinit var recycler: RecyclerView

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
        etSearch = findViewById(R.id.etSearchTask)
        spinnerFilter = findViewById(R.id.spinnerStatusFilter)

        // 1. Configurar adaptador inicial del RecyclerView
        val initialList = TaskViewModel.getTasks()
        taskAdapter = TaskListAdapter(initialList, this)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.adapter = taskAdapter

        // 2. Configurar el Spinner con las opciones de estado
        val statuses = arrayOf("TODOS", "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statuses)
        spinnerFilter.adapter = spinnerAdapter

        // 3. Listener para el buscador de texto en tiempo real
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 4. Listener para el cambio de selección en el Spinner
        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                applyFilters()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // 5. RETO EXTRA: Swipe to Delete con Snackbar y opción DESHACER
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val allVisibleTasks = TaskViewModel.getTasks() // O tu lista actual filtrada

                if (position in allVisibleTasks.indices) {
                    val taskToDelete = allVisibleTasks[position]

                    TaskMemoryManager().deleteTask(taskToDelete.id)
                    applyFilters() // Refrescar la vista

                    // Mostrar Snackbar con opción Deshacer
                    Snackbar.make(recycler, "Tarea eliminada", Snackbar.LENGTH_LONG)
                        .setAction("DESHACER") {
                            // Restaurar la tarea en memoria
                            TaskMemoryManager().addTask(taskToDelete)
                            applyFilters()
                        }.show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler)
    }

   //metodo de aplicación de filtros
    private fun applyFilters() {
        val query = etSearch.text.toString()
        val selectedPosition = spinnerFilter.selectedItemPosition

        val statusFilter = when (selectedPosition) {
            1 -> TaskStatus.PENDING
            2 -> TaskStatus.IN_PROGRESS
            3 -> TaskStatus.COMPLETED
            4 -> TaskStatus.CANCELLED
            else -> null
        }

        val filteredList = TaskViewModel.filterTasks(query, statusFilter)

        // Actualizamos el adaptador con la nueva lista filtrada
        taskAdapter = TaskListAdapter(filteredList, this)
        recycler.adapter = taskAdapter
    }

    override fun onResume() {
        super.onResume()
        applyFilters() // Refresca con los datos más recientes al volver a la pantalla
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }
}