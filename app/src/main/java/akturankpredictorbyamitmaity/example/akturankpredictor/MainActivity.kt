package akturankpredictorbyamitmaity.example.akturankpredictor

import akturankpredictorbyamitmaity.example.akturankpredictor.adapter.ExamAdapter
import akturankpredictorbyamitmaity.example.akturankpredictor.chat.ChatActivity
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Exam
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.ExamRepository
import akturankpredictorbyamitmaity.example.akturankpredictor.service.ExamService
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
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
    private lateinit var progressBar: ProgressBar
    private lateinit var aboutUs: CardView
    private lateinit var mentorGuideChat: CardView
    
    private lateinit var examService: ExamService
    private lateinit var examAdapter: ExamAdapter

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupExamService()
        setupRecyclerView()
        setupClickListeners()
        loadExams()
    }

    private fun initializeViews() {
        examRecyclerView = findViewById(R.id.exam_recycler_view)
        progressBar = findViewById(R.id.progress_bar)
        aboutUs = findViewById(R.id.about_us)
        mentorGuideChat = findViewById(R.id.mentor_guide_chat)
    }

    private fun setupExamService() {
        val examRepository = ExamRepository(this)
        examService = ExamService(examRepository)
    }

    private fun setupRecyclerView() {
        examAdapter = ExamAdapter(this, emptyList()) { exam ->
            Log.d(TAG, "Exam clicked: ${exam.name}")
            navigateToSelectRank(exam)
        }
        
        examRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = examAdapter
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
