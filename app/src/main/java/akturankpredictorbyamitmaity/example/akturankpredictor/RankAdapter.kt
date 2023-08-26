package akturankpredictorbyamitmaity.example.akturankpredictor

import android.content.Context
import android.graphics.ColorSpace.Model
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankAdapter(private val context: Context, private var mList: List<ModelClass>) : RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    // create new views



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.college_show_layout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val symptomsClass = mList[position]

        holder.collegeName.text = symptomsClass.Institute
        holder.category.text = symptomsClass.Category
        holder.branch.text = symptomsClass.Program
        holder.course.text = symptomsClass.Stream
        holder.quota.text = symptomsClass.Quota


    }

    fun filterList(filterlist: ArrayList<ModelClass>) {
        mList = filterlist
        notifyDataSetChanged()
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val collegeName: TextView = itemView.findViewById(R.id.college_name)
        val branch: TextView = itemView.findViewById(R.id.college_branch)
        val course: TextView = itemView.findViewById(R.id.college_course)
        val quota: TextView = itemView.findViewById(R.id.college_quota)
        val category: TextView = itemView.findViewById(R.id.college_category)
    }


}