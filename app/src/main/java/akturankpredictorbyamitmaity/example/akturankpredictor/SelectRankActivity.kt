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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectRankActivity : AppCompatActivity() {

    lateinit var textHeader:TextView
    lateinit var inputRank:TextView
    lateinit var submitButton:Button
    lateinit var only_for_jee_main_text:TextView


    companion object {
        var mInterstitialAd: InterstitialAd? = null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_rank)

        textHeader = findViewById(R.id.uptac_text_counsilname)
        inputRank = findViewById(R.id.user_rank_input)
        submitButton = findViewById(R.id.submit_button)
        only_for_jee_main_text = findViewById(R.id.only_for_jee_main_text)

        MobileAds.initialize(this) {}
        val mAdView: AdView = findViewById(R.id.select_rank_ADS)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        val adRequest1 = AdRequest.Builder().build()
        InterstitialAd.load(applicationContext,"ca-app-pub-3940256099942544/1033173712", adRequest1, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })



        val extras = intent.extras
        val str = extras?.getString("key")

        if(str.equals("aktu_btech.json")) textHeader.text = "AKTU B.Tech | Uttar Pradesh Technical Admission Counselling"
        if(str.equals("aktu_cuet.json")) textHeader.text = "AKTU CUET - UG | Uttar Pradesh Technical Admission Counselling"
        if(str.equals("hbtu_btech.json")) textHeader.text = "HBTU - B.Tech | Uttar Pradesh Technical Admission Counselling"
        if(str.equals("jee_main.json")) textHeader.text = "JEE Main/Adv | All India"


        var states = resources.getStringArray(R.array.quota)
        var quotas = resources.getStringArray(R.array.category)

        val spinner1 = findViewById<Spinner>(R.id.spinner_state)
        val spinner2 = findViewById<Spinner>(R.id.spinner_quota)



        if(str.equals("jee_main.json")){
            states = resources.getStringArray(R.array.jee_quota)
            quotas = resources.getStringArray(R.array.jee_category)
            only_for_jee_main_text.visibility = View.VISIBLE
        }

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

        if(!str.equals("jee_main.json")){
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
        }




        submitButton.setOnClickListener{
            if(inputRank.text.toString().equals("")){
                Toast.makeText(this,"Enter Your Rank",Toast.LENGTH_SHORT).show()
            }else{


                val intent = Intent(applicationContext, ShowCollegesActivity::class.java)
                intent.putExtra("division",str)
                intent.putExtra("rank",inputRank.text.toString())
                intent.putExtra("state",spinner1.selectedItem.toString())
                if(str.equals("jee_main.json")){
                    intent.putExtra("quota",spinner2.selectedItem.toString())
                    startActivity(intent)
                }else{
                    if(spinner1.selectedItem.toString().equals("Uttar Pradesh")) intent.putExtra("quota",spinner2.selectedItem.toString())
                    else if(spinner1.selectedItem.toString().equals("Select")) intent.putExtra("quota",spinner2.selectedItem.toString())
                    else intent.putExtra("quota","Select")

                    startActivity(intent)
                }



            }
        }



    }


}