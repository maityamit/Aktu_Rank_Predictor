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
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        contestOnly = ArrayList()
        progressBar = findViewById(R.id.progress_bar)

        val extras = intent.extras
        var division = extras?.getString("division")
        var rank = extras?.getString("rank")
        var state = extras?.getString("state")
        var quota = extras?.getString("quota")

//        Toast.makeText(this,division+" "+rank+" "+" "+state+" "+quota,Toast.LENGTH_LONG).show()

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

        getFetch(division,rank,state,quota)

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


}