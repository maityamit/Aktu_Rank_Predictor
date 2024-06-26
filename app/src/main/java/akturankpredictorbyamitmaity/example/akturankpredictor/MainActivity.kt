package akturankpredictorbyamitmaity.example.akturankpredictor

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var aktu_btech:LinearLayout
    lateinit var aktu_cuet:LinearLayout
    lateinit var hbtu_btech:LinearLayout
    lateinit var about_us: RelativeLayout
    lateinit var mentor_guide_chat:LinearLayout
    lateinit var jee_main_btech: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}



        aktu_btech = findViewById(R.id.aktu_btech)
        aktu_cuet = findViewById(R.id.aktu_cuet)
        hbtu_btech = findViewById(R.id.hbtu_btech)
        about_us = findViewById(R.id.about_us)
        mentor_guide_chat = findViewById(R.id.mentor_guide_chat)
        jee_main_btech = findViewById(R.id.jee_main_btech)



        mentor_guide_chat.setOnClickListener{

            val intent = Intent(this,ChatActivity::class.java)
            startActivity(intent)


        }




        about_us.setOnClickListener {

            about_us.setOnClickListener{
                val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                    .create()
                val view = layoutInflater.inflate(R.layout.myself_profile_layout,null)

                builder.setView(view)
                builder.setCanceledOnTouchOutside(true)
                builder.show()

                val myprofile_amit:LinearLayout =view.findViewById(R.id.myprofile_amit)
                val myprofile_nitish:LinearLayout = view.findViewById(R.id.myprofile_nitish)

                myprofile_amit.setOnClickListener{
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/maityamit"))
                    startActivity(browserIntent)
                }

                myprofile_nitish.setOnClickListener{
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/infiniteesh"))
                    startActivity(browserIntent)
                }

            }

        }


        aktu_btech.setOnClickListener {
            val intent = Intent(this, SelectRankActivity::class.java)
            intent.putExtra("key", "aktu_btech.json")
            startActivity(intent)
        }

        aktu_cuet.setOnClickListener {
            val intent = Intent(this, SelectRankActivity::class.java)
            intent.putExtra("key", "aktu_cuet.json")
            startActivity(intent)
        }

        hbtu_btech.setOnClickListener {
            val intent = Intent(this, SelectRankActivity::class.java)
            intent.putExtra("key", "hbtu_btech.json")
            startActivity(intent)
        }

        jee_main_btech.setOnClickListener{
            val intent = Intent(this, SelectRankActivity::class.java)
            intent.putExtra("key", "jee_main.json")
            startActivity(intent)
        }

    }


}