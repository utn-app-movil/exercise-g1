package cr.ac.utn.taskmanager

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var txtSearch: EditText
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

        val recycler= findViewById<RecyclerView>(R.id.rvTasks_List)
        taskAdapter = TaskListAdapter(TaskViewModel.getTasks(),
            this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter
        setupSwipeToDelete(recycler)

        txtSearch = findViewById(R.id.txtSearch_List)
        spStatus = findViewById(R.id.spStatus_List)

        val statusOptions = arrayOf("TODAS", "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED")
        val statusAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, statusOptions)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = statusAdapter

        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        txtSearch.doAfterTextChanged {
            filterList()
        }
    }

    override fun onResume() {
        super.onResume()
        filterList()
    }

    private fun filterList() {
        val text = txtSearch.text.toString()
        val status = spStatus.selectedItem.toString()
        taskAdapter.updateList(TaskViewModel.filterTasks(text, status))
    }

    private fun setupSwipeToDelete(recycler: RecyclerView) {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = taskAdapter.getTaskAt(viewHolder.bindingAdapterPosition)
                TaskViewModel.removeTask(task.id)
                filterList()

                val snackbar = Snackbar.make(recycler, R.string.TextDeleteSuccess, Snackbar.LENGTH_LONG)
                    .setAction(R.string.TextSnackbar) {
                        TaskViewModel.saveTask(task)
                        filterList()
                    }

                // Sube el mensaje para que no quede detras de la barra de navegacion
                val insets = ViewCompat.getRootWindowInsets(recycler)
                val navigationBarHeight = insets?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom ?: 0
                val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin = navigationBarHeight + 16
                snackbar.view.layoutParams = params

                snackbar.show()
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