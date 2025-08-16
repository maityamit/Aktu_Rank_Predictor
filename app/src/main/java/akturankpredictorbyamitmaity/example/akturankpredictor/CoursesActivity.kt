package akturankpredictorbyamitmaity.example.akturankpredictor

import akturankpredictorbyamitmaity.example.akturankpredictor.adapter.CourseAdapter
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Course
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.CourseRepository
import akturankpredictorbyamitmaity.example.akturankpredictor.service.CourseService
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class CoursesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var noResultsText: TextView
    private lateinit var headerText: TextView
    
    private lateinit var courseService: CourseService
    private lateinit var courseAdapter: CourseAdapter
    
    private var allCourses: List<Course> = emptyList()
    private var filteredCourses: List<Course> = emptyList()

    companion object {
        private const val TAG = "CoursesActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        initializeViews()
        setupCourseService()
        setupRecyclerView()
        setupSearchView()
        loadCourses()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.courses_recycler_view)
        progressBar = findViewById(R.id.progress_bar)
        searchView = findViewById(R.id.search_view)
        noResultsText = findViewById(R.id.no_results_text)
        headerText = findViewById(R.id.courses_header)
    }

    private fun setupCourseService() {
        val courseRepository = CourseRepository()
        courseService = CourseService(courseRepository)
    }

    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter(this, emptyList())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CoursesActivity)
            adapter = courseAdapter
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCourses(newText ?: "")
                return true
            }
        })
    }

    private fun loadCourses() {
        progressBar.visibility = View.VISIBLE
        Log.d(TAG, "Loading all courses...")
        
        lifecycleScope.launch {
            when (val response = courseService.getAvailableCourses()) {
                is ApiResponse.Success -> {
                    Log.d(TAG, "Courses loaded successfully: ${response.data.size} courses")
                    allCourses = response.data
                    filteredCourses = response.data
                    courseAdapter.updateCourses(filteredCourses)
                    progressBar.visibility = View.GONE
                    
                    updateHeaderText()
                    showResults()
                }
                is ApiResponse.Error -> {
                    Log.e(TAG, "Error loading courses: ${response.message}")
                    Toast.makeText(this@CoursesActivity, response.message, Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                    showNoResults()
                }
                is ApiResponse.Loading -> {
                    Log.d(TAG, "Loading courses...")
                }
            }
        }
    }

    private fun filterCourses(query: String) {
        filteredCourses = if (query.isBlank()) {
            allCourses
        } else {
            allCourses.filter { course ->
                course.name.contains(query, ignoreCase = true) ||
                course.description.contains(query, ignoreCase = true) ||
                course.mentors.any { it.contains(query, ignoreCase = true) }
            }
        }
        
        courseAdapter.updateCourses(filteredCourses)
        
        if (filteredCourses.isEmpty()) {
            showNoResults()
        } else {
            showResults()
        }
    }

    private fun showResults() {
        recyclerView.visibility = View.VISIBLE
        noResultsText.visibility = View.GONE
    }

    private fun showNoResults() {
        recyclerView.visibility = View.GONE
        noResultsText.visibility = View.VISIBLE
        noResultsText.text = "No courses found matching your search."
    }

    private fun updateHeaderText() {
        headerText.text = "All Courses (${allCourses.size})"
    }
}
