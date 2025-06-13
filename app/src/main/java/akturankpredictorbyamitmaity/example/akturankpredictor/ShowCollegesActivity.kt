package akturankpredictorbyamitmaity.example.akturankpredictor

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowCollegesActivity : AppCompatActivity() {

    lateinit var recyclerview: RecyclerView
    lateinit var contestOnly: ArrayList<ModelClass>
    lateinit var filteredList: ArrayList<ModelClass>
    lateinit var textShow: TextView
    lateinit var progressBar: ProgressBar
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_colleges)
        recyclerview = findViewById(R.id.show_colleges_recylcer)
        recyclerview.layoutManager = LinearLayoutManager(applicationContext)

        contestOnly = ArrayList()
        filteredList = ArrayList()
        progressBar = findViewById(R.id.progress_bar)
        searchView = findViewById(R.id.search_view)

        val extras = intent.extras
        val division = extras?.getString("division")
        val rank = extras?.getString("rank")
        val state = extras?.getString("state")
        val quota = extras?.getString("quota")

        textShow = findViewById(R.id.college_header)

        var asI: String
        var amI: String

        asI = if (state == "Select") "All" else state.toString()
        amI = if (quota == "Select") "All" else quota.toString()

        textShow.text = "Rank: $rank | State: $asI | Quota: $amI"

        if (division.equals("jee_main.json")) {
            getFetchForJeeMain(division, rank, state, quota)
        } else {
            getFetch(division, rank, state, quota)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })
    }

    private fun filter(text: String?) {
        filteredList.clear()
        if (text.isNullOrEmpty()) {
            filteredList.addAll(contestOnly)
        } else {
            contestOnly.forEach {
                if (it.Institute.contains(text, true)) {
                    filteredList.add(it)
                }
            }
        }
        recyclerview.adapter?.notifyDataSetChanged()
    }

    private fun getFetchForJeeMain(
        division: String?,
        rank: String?,
        state: String?,
        quota: String?
    ) {
        val destinationService = RankClient.buildService(RankAPISERVICE::class.java)
        val requestCallLeetCode = destinationService.getApiResponseAKTUBTECH(division)
        requestCallLeetCode.enqueue(object : Callback<List<ModelClass>> {
            override fun onResponse(
                call: Call<List<ModelClass>>,
                response: Response<List<ModelClass>>
            ) {
                if (response.isSuccessful) {
                    val symptomsList = response.body()!!
                    val iterator = symptomsList.iterator()
                    iterator.forEach {
                        if (rank != null && it.CR >= rank.toInt()) {
                            if (state == "Select" && quota == "Select") contestOnly.add(it)
                            if (state == "Select" && quota == it.Category) contestOnly.add(it)
                            if (quota == "Select" && state == it.Quota) contestOnly.add(it)
                            if (quota == it.Category && state == it.Quota) contestOnly.add(it)
                        }
                    }
                    filteredList.addAll(contestOnly)
                    if (contestOnly.size == 0) {
                        progressBar.isVisible = false
                        Toast.makeText(
                            applicationContext,
                            "Sorry! No Colleges Found",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    progressBar.isVisible = false
                    recyclerview.isVisible = true
                    recyclerview.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(applicationContext)
                        adapter = RankAdapter(applicationContext, filteredList)
                    }
                } else {
                    Toast.makeText(applicationContext, "Response Get Failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<ModelClass>>, t: Throwable) {
                Toast.makeText(applicationContext, "Failure + $t", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun getFetch(string: String?, rank: String?, state: String?, quota: String?) {
        val destinationService = RankClient.buildService(RankAPISERVICE::class.java)
        val requestCallLeetCode = destinationService.getApiResponseAKTUBTECH(string)
        requestCallLeetCode.enqueue(object : Callback<List<ModelClass>> {
            override fun onResponse(
                call: Call<List<ModelClass>>,
                response: Response<List<ModelClass>>
            ) {
                if (response.isSuccessful) {
                    val symptomsList = response.body()!!
                    val iterator = symptomsList.iterator()
                    iterator.forEach {
                        if (rank != null && it.CR >= rank.toInt()) {
                            if (state.equals("Uttar Pradesh") && it.Quota == "Home State") {
                                if (quota == "Select") {
                                    contestOnly.add(it)
                                } else if (quota == it.Category) contestOnly.add(it)
                            } else if (state.equals("Select")) {
                                if (quota == "Select") {
                                    contestOnly.add(it)
                                } else if (quota == it.Category) contestOnly.add(it)
                            } else if (it.Quota == "All India") {
                                if (quota == "Select") {
                                    contestOnly.add(it)
                                } else if (quota == it.Category) contestOnly.add(it)
                            }
                        }
                    }
                    filteredList.addAll(contestOnly)
                    if (contestOnly.size == 0) {
                        progressBar.isVisible = false
                        Toast.makeText(
                            applicationContext,
                            "Sorry! No Colleges Found",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    progressBar.isVisible = false
                    recyclerview.isVisible = true
                    recyclerview.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(applicationContext)
                        adapter = RankAdapter(applicationContext, filteredList)
                    }
                } else {
                    Toast.makeText(applicationContext, "Response Get Failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<ModelClass>>, t: Throwable) {
                Toast.makeText(applicationContext, "Failure + $t", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
