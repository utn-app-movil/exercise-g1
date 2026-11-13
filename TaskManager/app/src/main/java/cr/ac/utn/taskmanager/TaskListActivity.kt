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
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var etSearch: EditText
    private lateinit var spFilter: Spinner
    private val statusOptions = listOf("TODAS", "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED")

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
        taskAdapter = TaskListAdapter(TaskViewModel.getTasks(),this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter

        etSearch = findViewById<EditText>(R.id.etSearch_List)
        spFilter = findViewById<Spinner>(R.id.spStatus_List)

        val spAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFilter.adapter = spAdapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                applyFilters()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilters()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val task = taskAdapter.getTaskAt(position)
                TaskViewModel.removeTask(task.id)
                applyFilters()
                showUndoSnackbar(task)
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler)
    }

    override fun onResume() {
        super.onResume()
        applyFilters()
    }

    private fun applyFilters(){
        val text = etSearch.text.toString().trim()
        val status = if (spFilter.selectedItemPosition == 0) null
        else TaskStatus.valueOf(statusOptions[spFilter.selectedItemPosition])
        taskAdapter.updateList(TaskViewModel.filterTasks(text, status))
    }

    private fun showUndoSnackbar(task: Task){
        val snackbar = Snackbar.make(findViewById<View>(R.id.main),
            R.string.TextTaskDeleted, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.TextSnackbar, View.OnClickListener{ _ ->
            TaskViewModel.saveTask(task)
            applyFilters()
        })
        snackbar.show()
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }
}