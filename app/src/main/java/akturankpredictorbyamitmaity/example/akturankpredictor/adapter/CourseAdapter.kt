package akturankpredictorbyamitmaity.example.akturankpredictor.adapter

import akturankpredictorbyamitmaity.example.akturankpredictor.R
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Course
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class CourseAdapter(
    private val context: Context,
    private var courses: List<Course>
) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position]
        
        holder.courseName.text = course.name
        holder.coursePrice.text = course.price
        holder.courseMentors.text = course.mentors.joinToString(", ")
        holder.courseLastDate.text = "Last Date: ${course.last_date}"
        
        if (course.description.isNotEmpty()) {
            holder.courseDescription.text = course.description
            holder.courseDescription.visibility = View.VISIBLE
        } else {
            holder.courseDescription.visibility = View.GONE
        }
        
        if (course.duration.isNotEmpty()) {
            holder.courseDuration.text = course.duration
            holder.courseDuration.visibility = View.VISIBLE
        } else {
            holder.courseDuration.visibility = View.GONE
        }
        
        // Load course image with Glide
        Glide.with(context)
            .load(course.image)
            .apply(RequestOptions()
                .placeholder(R.drawable.kogo)
                .error(R.drawable.kogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
            )
            .into(holder.courseImage)
        
        // Handle course click - open link
        holder.itemView.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(course.link))
                context.startActivity(intent)
            } catch (e: Exception) {
                // Handle invalid URL
                android.widget.Toast.makeText(context, "Unable to open course link", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        // Show featured badge if course is featured
        if (course.is_featured) {
            holder.featuredBadge.visibility = View.VISIBLE
        } else {
            holder.featuredBadge.visibility = View.GONE
        }
        
        // Show rating if available
        if (course.rating > 0) {
            holder.courseRating.text = "★ ${course.rating}"
            holder.courseRating.visibility = View.VISIBLE
        } else {
            holder.courseRating.visibility = View.GONE
        }
        
        // Show students enrolled if available
        if (course.students_enrolled > 0) {
            holder.studentsEnrolled.text = "${course.students_enrolled} students enrolled"
            holder.studentsEnrolled.visibility = View.VISIBLE
        } else {
            holder.studentsEnrolled.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = courses.size

    fun updateCourses(newCourses: List<Course>) {
        courses = newCourses
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseImage: ImageView = itemView.findViewById(R.id.course_image)
        val courseName: TextView = itemView.findViewById(R.id.course_name)
        val coursePrice: TextView = itemView.findViewById(R.id.course_price)
        val courseMentors: TextView = itemView.findViewById(R.id.course_mentors)
        val courseLastDate: TextView = itemView.findViewById(R.id.course_last_date)
        val courseDescription: TextView = itemView.findViewById(R.id.course_description)
        val courseDuration: TextView = itemView.findViewById(R.id.course_duration)
        val courseRating: TextView = itemView.findViewById(R.id.course_rating)
        val studentsEnrolled: TextView = itemView.findViewById(R.id.students_enrolled)
        val featuredBadge: TextView = itemView.findViewById(R.id.featured_badge)
    }
}
