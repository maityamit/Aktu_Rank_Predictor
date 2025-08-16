package akturankpredictorbyamitmaity.example.akturankpredictor.adapter

import akturankpredictorbyamitmaity.example.akturankpredictor.R
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Exam
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExamAdapter(
    private val context: Context,
    private var exams: List<Exam>,
    private val onExamClick: (Exam) -> Unit
) : RecyclerView.Adapter<ExamAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "ExamAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "Creating ViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exam_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exam = exams[position]
        Log.d(TAG, "Binding exam: ${exam.name} at position $position")

        holder.examName.text = exam.name
        holder.examDescription.text = exam.description
        
        // Set dynamic icon
        try {
            val iconResourceId = context.resources.getIdentifier(
                exam.icon, 
                "drawable", 
                context.packageName
            )
            if (iconResourceId != 0) {
                holder.examIcon.setImageResource(iconResourceId)
            } else {
                // Fallback to default icon if the specified icon doesn't exist
                holder.examIcon.setImageResource(R.drawable.baseline_search_24)
                Log.w(TAG, "Icon resource not found: ${exam.icon}, using default icon")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting icon for exam ${exam.name}: ${e.message}")
            holder.examIcon.setImageResource(R.drawable.baseline_search_24)
        }

        holder.itemView.setOnClickListener {
            Log.d(TAG, "Exam clicked: ${exam.name}")
            onExamClick(exam)
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "Item count: ${exams.size}")
        return exams.size
    }

    fun updateExams(newExams: List<Exam>) {
        Log.d(TAG, "Updating exams: ${newExams.size} exams")
        exams = newExams
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val examIcon: ImageView = itemView.findViewById(R.id.exam_icon)
        val examName: TextView = itemView.findViewById(R.id.exam_name)
        val examDescription: TextView = itemView.findViewById(R.id.exam_description)
    }
}
