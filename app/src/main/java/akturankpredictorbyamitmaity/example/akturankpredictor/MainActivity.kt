package akturankpredictorbyamitmaity.example.akturankpredictor

import akturankpredictorbyamitmaity.example.akturankpredictor.adapter.CourseAdapter
import akturankpredictorbyamitmaity.example.akturankpredictor.adapter.ExamAdapter
import akturankpredictorbyamitmaity.example.akturankpredictor.chat.ChatActivity
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Course
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Exam
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.CourseRepository
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.ExamRepository
import akturankpredictorbyamitmaity.example.akturankpredictor.service.CourseService
import akturankpredictorbyamitmaity.example.akturankpredictor.service.ExamService
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var examRecyclerView: RecyclerView
    private lateinit var courseRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var courseProgressBar: ProgressBar
    private lateinit var aboutUs: CardView
    private lateinit var mentorGuideChat: CardView
    private lateinit var coursesSection: LinearLayout
    private lateinit var coursesHeaderSection: LinearLayout
    private lateinit var viewAllCoursesButton: TextView
    
    private lateinit var examService: ExamService
    private lateinit var courseService: CourseService
    private lateinit var examAdapter: ExamAdapter
    private lateinit var courseAdapter: CourseAdapter

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupServices()
        setupRecyclerViews()
        setupClickListeners()
        loadExams()
        loadCourses()
    }

    private fun initializeViews() {
        examRecyclerView = findViewById(R.id.exam_recycler_view)
        courseRecyclerView = findViewById(R.id.course_recycler_view)
        progressBar = findViewById(R.id.progress_bar)
        courseProgressBar = findViewById(R.id.course_progress_bar)
        aboutUs = findViewById(R.id.about_us)
        mentorGuideChat = findViewById(R.id.mentor_guide_chat)
        coursesSection = findViewById(R.id.courses_section)
        coursesHeaderSection = findViewById(R.id.courses_header_section)
        viewAllCoursesButton = findViewById(R.id.view_all_courses_button)
    }

    private fun setupServices() {
        val examRepository = ExamRepository(this)
        examService = ExamService(examRepository)
        
        val courseRepository = CourseRepository()
        courseService = CourseService(courseRepository)
    }

    private fun setupRecyclerViews() {
        // Setup Exam RecyclerView - Changed to VERTICAL
        examAdapter = ExamAdapter(this, emptyList()) { exam ->
            Log.d(TAG, "Exam clicked: ${exam.name}")
            navigateToSelectRank(exam)
        }
        
        examRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = examAdapter
        }
        
        // Setup Course RecyclerView - Keep HORIZONTAL for featured courses
        courseAdapter = CourseAdapter(this, emptyList())
        courseRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = courseAdapter
        }
    }

    private fun setupClickListeners() {
        mentorGuideChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        aboutUs.setOnClickListener {
            showAboutUsDialog()
        }
        
        // Courses header section click - navigate to courses
        coursesHeaderSection.setOnClickListener {
            val intent = Intent(this, CoursesActivity::class.java)
            startActivity(intent)
        }
        
        viewAllCoursesButton.setOnClickListener {
            // Navigate to CoursesActivity
            val intent = Intent(this, CoursesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadExams() {
        progressBar.visibility = View.VISIBLE
        Log.d(TAG, "Loading exams...")
        
        lifecycleScope.launch {
            when (val response = examService.getAvailableExams()) {
                is ApiResponse.Success -> {
                    Log.d(TAG, "Exams loaded successfully: ${response.data.size} exams")
                    examAdapter.updateExams(response.data)
                    progressBar.visibility = View.GONE
                }
                is ApiResponse.Error -> {
                    Log.e(TAG, "Error loading exams: ${response.message}")
                    Toast.makeText(this@MainActivity, response.message, Toast.LENGTH_LONG).show()
                    
                    // Fallback to hardcoded exams for testing
                    Log.d(TAG, "Using fallback hardcoded exams")
                    val fallbackExams = listOf(
                        Exam("jee_main", "JEE Main", "jee_main", "Joint Entrance Examination Main", "jee_main_logo", true),
                        Exam("jee_advance", "JEE Advance", "jee_advance", "Joint Entrance Examination Advance", "jee_advance_logo", true),
                        Exam("aktu_btech", "AKTU B.Tech", "aktu_btech", "Uttar Pradesh Technical Admission Counselling", "aktu_logo", true),
                        Exam("aktu_cuet", "AKTU CUET (UG)", "aktu_cuet", "Uttar Pradesh Technical Admission Counselling - CUET", "aktu_cuet_logo", true),
                        Exam("hbtu_btech", "HBTU B.Tech", "hbtu_btech", "Harcourt Butler Technical University", "hbtu_logo", true)
                    )
                    examAdapter.updateExams(fallbackExams)
                    progressBar.visibility = View.GONE
                }
                is ApiResponse.Loading -> {
                    Log.d(TAG, "Loading exams...")
                }
            }
        }
    }

    private fun loadCourses() {
        courseProgressBar.visibility = View.VISIBLE
        Log.d(TAG, "Loading courses...")
        
        lifecycleScope.launch {
            when (val response = courseService.getAvailableCourses()) {
                is ApiResponse.Success -> {
                    Log.d(TAG, "Courses loaded successfully: ${response.data.size} courses")
                    val featuredCourses = courseService.getFeaturedCourses(response.data)
                    courseAdapter.updateCourses(featuredCourses)
                    courseProgressBar.visibility = View.GONE
                    
                    // Show courses section if we have courses
                    if (featuredCourses.isNotEmpty()) {
                        coursesSection.visibility = View.VISIBLE
                    }
                }
                is ApiResponse.Error -> {
                    Log.e(TAG, "Error loading courses: ${response.message}")
                    // Don't show error toast for courses as it's not critical
                    courseProgressBar.visibility = View.GONE
                    
                    // Hide courses section if no courses available
                    coursesSection.visibility = View.GONE
                }
                is ApiResponse.Loading -> {
                    Log.d(TAG, "Loading courses...")
                }
            }
        }
    }

    private fun navigateToSelectRank(exam: Exam) {
        Log.d(TAG, "Navigating to SelectRankActivity with exam: ${exam.name}")
        val intent = Intent(this, SelectRankActivity::class.java)
        intent.putExtra("exam_id", exam.id)
        intent.putExtra("exam_name", exam.name)
        startActivity(intent)
    }

    private fun showAboutUsDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.myself_profile_layout, null)
        builder.setView(view)
        builder.setCanceledOnTouchOutside(true)
        builder.show()
        
        val myprofileAmit: LinearLayout = view.findViewById(R.id.myprofile_amit)
        val myprofileNitish: LinearLayout = view.findViewById(R.id.myprofile_nitish)
        
        myprofileAmit.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.linkedin.com/in/maityamit")
            )
            startActivity(browserIntent)
        }
        
        myprofileNitish.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.linkedin.com/in/infiniteesh")
            )
            startActivity(browserIntent)
        }
    }
}
