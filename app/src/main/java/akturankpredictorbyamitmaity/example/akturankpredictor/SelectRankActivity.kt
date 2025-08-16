package akturankpredictorbyamitmaity.example.akturankpredictor

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.FilterOptions
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.UserPreferences
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.ExamRepository
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.FilterOptionsRepository
import akturankpredictorbyamitmaity.example.akturankpredictor.service.ExamService
import akturankpredictorbyamitmaity.example.akturankpredictor.service.FilterOptionsService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SelectRankActivity : AppCompatActivity() {

    private lateinit var textHeader: TextView
    private lateinit var examIcon: ImageView
    private lateinit var inputRank: EditText
    private lateinit var submitButton: Button
    private lateinit var submitButtonProgress: ProgressBar
    private lateinit var stateSpinner: Spinner
    private lateinit var genderSpinner: Spinner
    private lateinit var quotaSpinner: Spinner
    private lateinit var progressBar: View
    
    private var examId: String = ""
    private var examName: String = ""
    private var examIconName: String = ""
    private lateinit var filterOptionsService: FilterOptionsService
    private lateinit var examService: ExamService

    companion object {
        private const val TAG = "SelectRankActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_rank)

        initializeViews()
        setupIntentData()
        setupServices()
        loadExamData()
        loadFilterOptions()
        setupSubmitButton()
    }

    private fun initializeViews() {
        textHeader = findViewById(R.id.uptac_text_counsilname)
        examIcon = findViewById(R.id.exam_icon)
        inputRank = findViewById(R.id.user_rank_input)
        submitButton = findViewById(R.id.submit_button)
        submitButtonProgress = findViewById(R.id.submit_button_progress)
        stateSpinner = findViewById(R.id.spinner_state)
        genderSpinner = findViewById(R.id.spinner_gender)
        quotaSpinner = findViewById(R.id.spinner_quota)
        progressBar = findViewById(R.id.progress_bar)
    }

    private fun setupIntentData() {
        examId = intent.getStringExtra("exam_id") ?: ""
        examName = intent.getStringExtra("exam_name") ?: ""
        
        // Update header to show exam name
        textHeader.text = "$examName - Enter Your Details"
    }

    private fun setupServices() {
        val filterOptionsRepository = FilterOptionsRepository(this)
        filterOptionsService = FilterOptionsService(filterOptionsRepository)
        
        val examRepository = ExamRepository(this)
        examService = ExamService(examRepository)
    }

    private fun loadExamData() {
        lifecycleScope.launch {
            when (val response = examService.getAvailableExams()) {
                is ApiResponse.Success -> {
                    val exam = response.data.find { it.id == examId }
                    exam?.let { 
                        examIconName = it.icon
                        setExamIcon()
                        updateRankHint(examName)
                    }
                }
                is ApiResponse.Error -> {
                    Log.e(TAG, "Error loading exam data: ${response.message}")
                }
                is ApiResponse.Loading -> {
                    Log.d(TAG, "Loading exam data...")
                }
            }
        }
    }

    private fun setExamIcon() {
        try {
            val iconResourceId = resources.getIdentifier(
                examIconName, 
                "drawable", 
                packageName
            )
            if (iconResourceId != 0) {
                examIcon.setImageResource(iconResourceId)
            } else {
                examIcon.setImageResource(R.drawable.baseline_search_24)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting exam icon: ${e.message}")
            examIcon.setImageResource(R.drawable.baseline_search_24)
        }
    }

    private fun updateRankHint(examName: String) {
        inputRank.hint = "Enter your rank in $examName"
    }

    private fun loadFilterOptions() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            when (val response = filterOptionsService.getFilterOptions()) {
                is ApiResponse.Success -> {
                    setupSpinners(response.data)
                    progressBar.visibility = View.GONE
                }
                is ApiResponse.Error -> {
                    Toast.makeText(this@SelectRankActivity, response.message, Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                    
                    // Fallback to hardcoded options
                    setupFallbackSpinners()
                }
                is ApiResponse.Loading -> {
                    Log.d(TAG, "Loading filter options...")
                }
            }
        }
    }

    private fun setupSpinners(filterOptions: FilterOptions) {
        // State options
        val stateAdapter = ArrayAdapter(this, R.layout.spinner_item, filterOptions.states)
        stateSpinner.adapter = stateAdapter

        // Gender options
        val genderAdapter = ArrayAdapter(this, R.layout.spinner_item, filterOptions.genders)
        genderSpinner.adapter = genderAdapter

        // Quota options
        val quotaAdapter = ArrayAdapter(this, R.layout.spinner_item, filterOptions.quotas)
        quotaSpinner.adapter = quotaAdapter

    }

    private fun setupFallbackSpinners() {

        // Fallback state options
        val fallbackStates = arrayOf("All", "Uttar Pradesh", "Delhi", "Maharashtra", "Karnataka")
        val stateAdapter = ArrayAdapter(this, R.layout.spinner_item, fallbackStates)
        stateSpinner.adapter = stateAdapter

        // Fallback gender options
        val fallbackGenders = arrayOf("All", "Male", "Female")
        val genderAdapter = ArrayAdapter(this, R.layout.spinner_item, fallbackGenders)
        genderSpinner.adapter = genderAdapter

        // Fallback quota options
        val fallbackQuotas = arrayOf("All", "General", "Ews", "Obc", "Sc", "St")
        val quotaAdapter = ArrayAdapter(this, R.layout.spinner_item, fallbackQuotas)
        quotaSpinner.adapter = quotaAdapter
    }

    private fun setupSubmitButton() {
        submitButton.setOnClickListener {
            if (validateInput()) {
                setButtonLoading(true)
                val userPreferences = createUserPreferences()
                navigateToShowColleges(userPreferences)
            }
        }
    }

    private fun setButtonLoading(isLoading: Boolean) {
        if (isLoading) {
            submitButton.isEnabled = false
            submitButton.text = "Finding Colleges..."
            submitButtonProgress.visibility = View.VISIBLE
        } else {
            submitButton.isEnabled = true
            submitButton.text = "Find Colleges"
            submitButtonProgress.visibility = View.GONE
        }
    }

    private fun validateInput(): Boolean {
        val rankText = inputRank.text.toString()
        
        if (rankText.isEmpty()) {
            Toast.makeText(this, "Please enter your rank", Toast.LENGTH_SHORT).show()
            return false
        }
        
        val rank = rankText.toIntOrNull()
        if (rank == null || rank <= 0) {
            Toast.makeText(this, "Please enter a valid rank", Toast.LENGTH_SHORT).show()
            return false
        }
        
        return true
    }

    private fun createUserPreferences(): UserPreferences {
        return UserPreferences(
            rank = inputRank.text.toString().toInt(),
            state = stateSpinner.selectedItem.toString(),
            gender = genderSpinner.selectedItem.toString(),
            quota = quotaSpinner.selectedItem.toString()
        )
    }

    private fun navigateToShowColleges(userPreferences: UserPreferences) {
        val intent = Intent(this, ShowCollegesActivity::class.java)
        intent.putExtra("exam_id", examId)
        intent.putExtra("exam_name", examName)
        intent.putExtra("user_rank", userPreferences.rank)
        intent.putExtra("user_state", userPreferences.state)
        intent.putExtra("user_gender", userPreferences.gender)
        intent.putExtra("user_quota", userPreferences.quota)
        startActivity(intent)
        
        // Reset button state after navigation
        setButtonLoading(false)
    }
}
