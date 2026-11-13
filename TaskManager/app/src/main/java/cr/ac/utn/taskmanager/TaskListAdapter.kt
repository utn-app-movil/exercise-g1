package cr.ac.utn.taskmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.taskmanager.domain.model.Task

class TaskListAdapter(
    private var tasks: List<Task>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_task_item, parent, false)
        return CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(tasks[position], itemClickListener)
    }

    override fun getItemCount(): Int = tasks.size

    fun getTaskAt(position: Int): Task = tasks[position]

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks.toList()
        notifyDataSetChanged()
    }
}

class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvTitle = view.findViewById<TextView>(R.id.txtTitle_Item)
    private val tvStatus = view.findViewById<TextView>(R.id.txtStatus_Item)
    private val tvPriority = view.findViewById<TextView>(R.id.txtPriority_Item)
    private val tvAssignTo = view.findViewById<TextView>(R.id.txtAssignTo_Item)
    private val imgPicture = view.findViewById<ImageView>(R.id.imgPicture_Item)

    fun bind(task: Task, itemClickListener: OnItemClickListener) {
        tvTitle.text = task.title
        tvStatus.text = task.status.name
        tvPriority.text = task.priority.name
        tvAssignTo.text = task.assignedTo
        imgPicture.setImageResource(R.drawable.ic_launcher_foreground)
        itemView.setOnClickListener { itemClickListener.onItemClick(task) }
    }
}
