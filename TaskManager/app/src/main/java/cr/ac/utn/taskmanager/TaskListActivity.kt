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
import android.widget.ArrayAdapter
import android.widget.Spinner
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.view.View
import android.widget.AdapterView

class TaskListActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var taskAdapter: TaskListAdapter
    private var allTasks: List<Task> = emptyList()

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
        allTasks = TaskViewModel.getTasks()
        taskAdapter = TaskListAdapter(allTasks, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter
        val spStatus = findViewById<Spinner>(R.id.spStatus_List)
        val options = listOf("TODAS") + TaskStatus.values().map { it.name }
        spStatus.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)

        val edtSearch = findViewById<EditText>(R.id.edtSearch_List)
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                applyFilters(s.toString(), spStatus.selectedItem.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilters(edtSearch.text.toString(), spStatus.selectedItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onResume() {
        super.onResume()
        allTasks = TaskViewModel.getTasks()
        val edtSearch = findViewById<EditText>(R.id.edtSearch_List)
        val spStatus = findViewById<Spinner>(R.id.spStatus_List)
        applyFilters(edtSearch.text.toString(), spStatus.selectedItem.toString())
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }

    private fun applyFilters(text: String, status: String) {
        val filtered = allTasks.filter { task ->
            val matchesText = task.title.contains(text, ignoreCase = true)
            val matchesStatus = status == "TODAS" || task.status.name == status
            matchesText && matchesStatus
        }
        taskAdapter.updateTasks(filtered)
    }
}