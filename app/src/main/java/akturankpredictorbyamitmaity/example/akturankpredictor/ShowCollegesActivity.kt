package akturankpredictorbyamitmaity.example.akturankpredictor

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowCollegesActivity : AppCompatActivity() {

    lateinit var recyclerview: RecyclerView
    lateinit var contestOnly: ArrayList<ModelClass>
    lateinit var textShow: TextView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_colleges)
        recyclerview = findViewById<RecyclerView>(R.id.show_colleges_recylcer)
        recyclerview.layoutManager = LinearLayoutManager(applicationContext)


        MobileAds.initialize(this) {}
        val mAdView: AdView = findViewById(R.id.show_colleges_ADS)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)



        contestOnly = ArrayList()
        progressBar = findViewById(R.id.progress_bar)

        val extras = intent.extras
        val division = extras?.getString("division")
        val rank = extras?.getString("rank")
        val state = extras?.getString("state")
        val quota = extras?.getString("quota")

    //    Toast.makeText(this,division+" "+rank+" "+" "+state+" "+quota,Toast.LENGTH_LONG).show()

        textShow = findViewById(R.id.college_header)

        var asI:String
        var amI:String

        asI = String()
        amI = String()


        if(state=="Select") asI = "All"
        else asI= state.toString()
        if(quota=="Select") amI = "All"
        else amI= quota.toString()



        textShow.text = "Rank: $rank | State: $asI | Quota: $amI"


        if(division.equals("jee_main.json")){
            getFetchForJeeMain(division,rank,state,quota)
        }else{
            getFetch(division,rank,state,quota)
        }

    }

    private fun getFetchForJeeMain(division: String?, rank: String?, state: String?, quota: String?) {
        val destinationService  = RankClient.buildService(RankAPISERVICE::class.java)
        val requestCallLeetCode =destinationService.getApiResponseAKTUBTECH(division)
        //make network call asynchronously
        requestCallLeetCode.enqueue(object : Callback<List<ModelClass>> {
            override fun onResponse(call: Call<List<ModelClass>>, response: Response<List<ModelClass>>) {
                if (response.isSuccessful){
                    val symptomsList  = response.body()!!
                    val iterator = symptomsList.iterator()
                   // Toast.makeText(applicationContext,state,Toast.LENGTH_SHORT).show()
                    iterator.forEach {
                        if(rank!=null && it.CR>=rank.toInt()) {
                            if(state=="Select" && quota=="Select") contestOnly.add(it)
                            if(state=="Select" && quota==it.Category) contestOnly.add(it)
                            if(quota=="Select" && state==it.Quota) contestOnly.add(it)
                            if(quota==it.Category && state==it.Quota) contestOnly.add(it)
                        }
                    }

//                    Toast.makeText(applicationContext,contestOnly.size.toString(),Toast.LENGTH_SHORT).show()

                    if(contestOnly.size == 0){
                        progressBar.isVisible = false
                        Toast.makeText(applicationContext,"Sorry! No Colleges Found",Toast.LENGTH_LONG).show()
                    }

                    progressBar.isVisible = false
                    recyclerview.isVisible = true
                    recyclerview.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(applicationContext)
                        adapter = RankAdapter(applicationContext,contestOnly)
                    }


                }else{
                    Toast.makeText(applicationContext, "Response Get Failed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<ModelClass>>, t: Throwable) {
                Toast.makeText(applicationContext, "Failure + ${t.toString()}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getFetch(string: String?,rank:String?,state:String?,quota:String?){

        val destinationService  = RankClient.buildService(RankAPISERVICE::class.java)
        val requestCallLeetCode =destinationService.getApiResponseAKTUBTECH(string)
        //make network call asynchronously
        requestCallLeetCode.enqueue(object : Callback<List<ModelClass>> {
            override fun onResponse(call: Call<List<ModelClass>>, response: Response<List<ModelClass>>) {
                if (response.isSuccessful){
                    val symptomsList  = response.body()!!

                    val iterator = symptomsList.iterator()
                    iterator.forEach {
                            if(rank!=null && it.CR>=rank.toInt()) {
                                if(state.equals("Uttar Pradesh") && it.Quota == "Home State"){
                                    if(quota=="Select") contestOnly.add(it)
                                    else if(quota==it.Category) contestOnly.add(it)
                                }else if(state.equals("Select")){
                                    if(quota=="Select") contestOnly.add(it)
                                    else if(quota==it.Category) contestOnly.add(it)
                                }else if(it.Quota == "All India"){
                                    if(quota=="Select") contestOnly.add(it)
                                    else if(quota==it.Category) contestOnly.add(it)
                                }
                            }
                    }

//                    Toast.makeText(applicationContext,contestOnly.size.toString(),Toast.LENGTH_SHORT).show()

                    if(contestOnly.size ==0){
                        progressBar.isVisible = false
                        Toast.makeText(applicationContext,"Sorry! No Colleges Found",Toast.LENGTH_LONG).show()
                    }

                    progressBar.isVisible = false
                    recyclerview.isVisible = true
                    recyclerview.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(applicationContext)
                        adapter = RankAdapter(applicationContext,contestOnly)
                    }


                }else{
                    Toast.makeText(applicationContext, "Response Get Failed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<ModelClass>>, t: Throwable) {
                Toast.makeText(applicationContext, "Failure + ${t.toString()}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onStart() {
        super.onStart()
        SelectRankActivity.mInterstitialAd?.show(this@ShowCollegesActivity)
    }


}