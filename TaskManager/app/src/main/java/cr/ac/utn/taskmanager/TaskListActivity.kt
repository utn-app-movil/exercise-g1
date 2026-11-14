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

        svSearch = findViewById<SearchView>(R.id.svSearch_List)
        spStatus = findViewById<Spinner>(R.id.spStatus_List)

        val recycler= findViewById<RecyclerView>(R.id.rvTasks_List)
        taskAdapter = TaskListAdapter(TaskViewModel.getTasks().toList(),
            this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter

        setupStatusSpinner()

        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList()
                return true
            }
        })

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = taskAdapter.getTask(viewHolder.bindingAdapterPosition)
                TaskViewModel.removeTask(task.id)
                filterList()

                val snackbar = Snackbar.make(findViewById(R.id.main),
                    getString(R.string.TextTaskDeleted), Snackbar.LENGTH_LONG)
                snackbar.setAction(getString(R.string.TextUndo), View.OnClickListener {
                    TaskViewModel.saveTask(task)
                    filterList()
                })
                snackbar.show()
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler)
    }

    override fun onResume() {
        super.onResume()
        filterList()
    }

    private fun setupStatusSpinner(){
        val options = mutableListOf(getString(R.string.TextAll))
        for (status in TaskStatus.entries){
            options.add(status.name)
        }

        val statusAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, options)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = statusAdapter

        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                filterList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun filterList(){
        val text = svSearch.query.toString().trim()
        val position = spStatus.selectedItemPosition
        // la posicion 0 es TODAS, las demas son los estados en orden
        val status = if (position > 0) TaskStatus.entries[position - 1] else null
        taskAdapter.updateList(TaskViewModel.filterTasks(text, status))
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.title)
    }
}
