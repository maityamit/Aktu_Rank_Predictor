package akturankpredictorbyamitmaity.example.akturankpredictor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectRankActivity : AppCompatActivity() {

    lateinit var textHeader:TextView
    lateinit var inputRank:TextView
    lateinit var submitButton:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_rank)

        textHeader = findViewById(R.id.uptac_text_counsilname)
        inputRank = findViewById(R.id.user_rank_input)
        submitButton = findViewById(R.id.submit_button)

        val extras = intent.extras
        var str = extras?.getString("key")

        if(str.equals("aktu_btech.json")) textHeader.text = "AKTU B.Tech | Uttar Pradesh Technical Admission Counselling"
        if(str.equals("aktu_cuet.json")) textHeader.text = "AKTU CUET - UG | Uttar Pradesh Technical Admission Counselling"
        if(str.equals("hbtu_btech.json")) textHeader.text = "HBTU - B.Tech | Uttar Pradesh Technical Admission Counselling"


        val states = resources.getStringArray(R.array.quota)
        val quotas = resources.getStringArray(R.array.category)
        val spinner1 = findViewById<Spinner>(R.id.spinner_state)
        val spinner2 = findViewById<Spinner>(R.id.spinner_quota)
        if (spinner1 != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_item, states
            )
            spinner1.adapter = adapter
        }

        if (spinner2 != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_item, quotas
            )
            spinner2.adapter = adapter
        }

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinner2.isVisible = id.toInt() != 2
            }
        }

        submitButton.setOnClickListener{
            if(inputRank.text.toString().equals("")){
                Toast.makeText(this,"Enter Your Rank",Toast.LENGTH_SHORT).show()
            }else{


                var intent = Intent(applicationContext, ShowCollegesActivity::class.java)
                intent.putExtra("division",str)
                intent.putExtra("rank",inputRank.text.toString())
                intent.putExtra("state",spinner1.selectedItem.toString())

                if(spinner1.selectedItem.toString().equals("Uttar Pradesh")) intent.putExtra("quota",spinner2.selectedItem.toString())
                else if(spinner1.selectedItem.toString().equals("Select")) intent.putExtra("quota",spinner2.selectedItem.toString())
                else intent.putExtra("quota","Select")

                startActivity(intent)

            }
        }



    }


}