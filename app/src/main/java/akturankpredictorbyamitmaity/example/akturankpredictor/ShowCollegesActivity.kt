package akturankpredictorbyamitmaity.example.akturankpredictor

import akturankpredictorbyamitmaity.example.akturankpredictor.adapter.CollegeAdapter
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.College
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.UserPreferences
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.CollegeRepository
import akturankpredictorbyamitmaity.example.akturankpredictor.service.CollegeService
import android.os.Bundle
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

class ShowCollegesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var headerText: TextView
    private lateinit var noResultsText: TextView
    private lateinit var loadingText: TextView
    private lateinit var loadingContainer: LinearLayout
    
    private lateinit var collegeService: CollegeService
    private lateinit var collegeAdapter: CollegeAdapter
    
    private var allColleges: List<College> = emptyList()
    private var filteredColleges: List<College> = emptyList()
    
    private var examId: String = ""
    private var examName: String = ""
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_colleges)

        initializeViews()
        setupIntentData()
        setupCollegeService()
        setupRecyclerView()
        setupSearchView()
        loadColleges()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.show_colleges_recylcer)
        progressBar = findViewById(R.id.progress_bar)
        searchView = findViewById(R.id.search_view)
        headerText = findViewById(R.id.college_header)
        noResultsText = findViewById(R.id.no_results_text)
        loadingText = findViewById(R.id.loading_text)
        loadingContainer = findViewById(R.id.loading_container)
    }

    private fun setupIntentData() {
        examId = intent.getStringExtra("exam_id") ?: ""
        examName = intent.getStringExtra("exam_name") ?: ""
        
        userPreferences = UserPreferences(
            rank = intent.getIntExtra("user_rank", 0),
            state = intent.getStringExtra("user_state") ?: "",
            gender = intent.getStringExtra("user_gender") ?: "",
            quota = intent.getStringExtra("user_quota") ?: ""
        )
        
        updateHeaderText()
    }

    private fun setupCollegeService() {
        val collegeRepository = CollegeRepository()
        collegeService = CollegeService(collegeRepository)
    }

    private fun setupRecyclerView() {
        collegeAdapter = CollegeAdapter(this, emptyList())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ShowCollegesActivity)
            adapter = collegeAdapter
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterColleges(newText ?: "")
                return true
            }
        })
    }

    private fun updateHeaderText() {
        val header = "Rank: ${userPreferences.rank} | " +
                    "State: ${userPreferences.state} | " +
                    "Gender: ${userPreferences.gender} | " +
                    "Quota: ${userPreferences.quota}"
        headerText.text = header
    }

    private fun loadColleges() {
        showLoadingState()
        
        lifecycleScope.launch {
            when (val response = collegeService.getCollegesForUser(examId, userPreferences)) {
                is ApiResponse.Success -> {
                    allColleges = response.data
                    filteredColleges = allColleges
                    
                    if (allColleges.isEmpty()) {
                        showNoResults()
                    } else {
                        showResults()
                    }
                }
                is ApiResponse.Error -> {
                    Toast.makeText(this@ShowCollegesActivity, response.message, Toast.LENGTH_LONG).show()
                    showNoResults()
                }
                is ApiResponse.Loading -> {
                    // Already showing loading state
                }
            }
        }
    }

    private fun showLoadingState() {
        loadingContainer.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        noResultsText.visibility = View.GONE
        searchView.visibility = View.GONE
        
        loadingText.text = "Finding colleges for your preferences..."
    }

    private fun filterColleges(query: String) {
        filteredColleges = collegeService.searchColleges(allColleges, query)
        collegeAdapter.updateColleges(filteredColleges)
        
        if (filteredColleges.isEmpty() && query.isNotEmpty()) {
            noResultsText.visibility = View.VISIBLE
            noResultsText.text = "No colleges found matching '$query'"
        } else {
            noResultsText.visibility = View.GONE
        }
    }

    private fun showResults() {
        loadingContainer.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        searchView.visibility = View.VISIBLE
        noResultsText.visibility = View.GONE
        
        collegeAdapter.updateColleges(filteredColleges)
        
        // Show success message
        val message = "Found ${filteredColleges.size} colleges for your preferences"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showNoResults() {
        loadingContainer.visibility = View.GONE
        recyclerView.visibility = View.GONE
        searchView.visibility = View.GONE
        noResultsText.visibility = View.VISIBLE
        
        noResultsText.text = "No colleges found for your preferences.\n\nTry adjusting your filters:\n• Lower your rank\n• Change your state\n• Select different quota"
    }
}
