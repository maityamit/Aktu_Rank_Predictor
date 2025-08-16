package akturankpredictorbyamitmaity.example.akturankpredictor.adapter

import akturankpredictorbyamitmaity.example.akturankpredictor.R
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.College
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CollegeAdapter(
    private val context: Context,
    private var colleges: List<College>
) : RecyclerView.Adapter<CollegeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.college_show_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val college = colleges[position]
        
        holder.collegeName.text = college.institute
        holder.course.text = college.course
        holder.quota.text = college.quota
        holder.stateQuota.text = college.state_quota
        holder.gender.text = college.gender
        holder.rankRange.text = "OR: ${college.or} | CR: ${college.cr}"
        holder.state.text = college.state
    }

    override fun getItemCount(): Int = colleges.size

    fun updateColleges(newColleges: List<College>) {
        colleges = newColleges
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val collegeName: TextView = itemView.findViewById(R.id.college_name)
        val course: TextView = itemView.findViewById(R.id.college_course)
        val quota: TextView = itemView.findViewById(R.id.college_quota)
        val stateQuota: TextView = itemView.findViewById(R.id.college_state_quota)
        val gender: TextView = itemView.findViewById(R.id.college_gender)
        val rankRange: TextView = itemView.findViewById(R.id.college_rank_range)
        val state: TextView = itemView.findViewById(R.id.college_state)
    }
}
