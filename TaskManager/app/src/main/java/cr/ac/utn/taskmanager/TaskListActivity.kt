
package cr.ac.utn.taskmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
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

    private lateinit var recycler: RecyclerView
    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var searchView: SearchView
    private lateinit var spStatus: Spinner

    private var searchText = ""
    private var selectedStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        recycler = findViewById(R.id.rvTasks_List)
        searchView = findViewById(R.id.searchTasks_List)
        spStatus = findViewById(R.id.spStatus_List)

        taskAdapter = TaskListAdapter(
            TaskViewModel.getTasks().toList(),
            this
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = taskAdapter

        // Búsqueda dinámica por texto
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(
                    query: String?
                ): Boolean {
                    return true
                }

                override fun onQueryTextChange(
                    newText: String?
                ): Boolean {
                    searchText = newText ?: ""
                    filterTasks()
                    return true
                }
            }
        )

        // Filtro por estado
        spStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedStatus = position
                    filterTasks()
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                    selectedStatus = 0
                    filterTasks()
                }
            }

        // Eliminar tarea deslizando
        val swipeCallback = object :
            ItemTouchHelper.SimpleCallback(
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
                val position = viewHolder.bindingAdapterPosition

                if (position == RecyclerView.NO_POSITION) {
                    return
                }

                val deletedTask = taskAdapter.getTaskAt(position)

                TaskViewModel.removeTask(deletedTask.id)
                filterTasks()


                Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.TextDeleteSuccess,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.TextSnackbar) {

                    TaskViewModel.saveTask(deletedTask)
                    filterTasks()

                }.show()

            }
        }

        ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler)

        filterTasks()
    }

    override fun onResume() {
        super.onResume()

        if (::taskAdapter.isInitialized) {
            filterTasks()
        }
    }

    private fun filterTasks() {

        val status = when (selectedStatus) {
            1 -> TaskStatus.PENDING
            2 -> TaskStatus.IN_PROGRESS
            3 -> TaskStatus.COMPLETED
            4 -> TaskStatus.CANCELLED
            else -> null
        }

        val filteredTasks = TaskViewModel.getTasks().filter { task ->

            val matchesText =
                task.title.contains(searchText, ignoreCase = true) ||
                        task.description.contains(searchText, ignoreCase = true)

            val matchesStatus =
                status == null || task.status == status

            matchesText && matchesStatus
        }

        taskAdapter.updateTasks(filteredTasks)
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
