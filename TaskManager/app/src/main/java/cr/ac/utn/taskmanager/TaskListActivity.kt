
package cr.ac.utn.taskmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var etSearchTask: EditText
    private lateinit var spFilterStatus: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)
        etSearchTask = findViewById(R.id.etSearchTask)
        spFilterStatus = findViewById(R.id.spFilterStatus)

        taskAdapter = TaskListAdapter(
            TaskViewModel.getTasks(),
            this
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = taskAdapter

        setupSpinner()
        setupSearch()
    }

    private fun setupSpinner() {

        val statuses = listOf("ALL") +
                TaskStatus.entries.map { it.name }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statuses
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spFilterStatus.adapter = adapter

        spFilterStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    filterTasks()
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }
    }

    private fun setupSearch() {

        etSearchTask.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    filterTasks()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )
    }

    private fun filterTasks() {

        val searchText = etSearchTask.text.toString().trim()

        val selectedStatus =
            spFilterStatus.selectedItem?.toString() ?: "ALL"

        val filteredTasks = TaskViewModel.getTasks().filter { task ->

            val matchesSearch =
                task.title.contains(searchText, ignoreCase = true)

            val matchesStatus =
                selectedStatus == "ALL" ||
                        task.status.name == selectedStatus

            matchesSearch && matchesStatus
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

    override fun onResume() {
        super.onResume()

        if (::taskAdapter.isInitialized) {
            filterTasks()
        }
    }
}
