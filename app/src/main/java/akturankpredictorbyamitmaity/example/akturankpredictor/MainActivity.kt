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
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var aktu_btech:LinearLayout
    lateinit var aktu_cuet:LinearLayout
    lateinit var hbtu_btech:LinearLayout
    lateinit var about_us: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aktu_btech = findViewById(R.id.aktu_btech)
        aktu_cuet = findViewById(R.id.aktu_cuet)
        hbtu_btech = findViewById(R.id.hbtu_btech)
        about_us = findViewById(R.id.about_us)



        about_us.setOnClickListener {

            about_us.setOnClickListener{
                val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                    .create()
                val view = layoutInflater.inflate(R.layout.myself_profile_layout,null)


                builder.setView(view)
                builder.setCanceledOnTouchOutside(true)
                builder.show()

                view.setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/maityamit"))
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

    }

}