package akturankpredictorbyamitmaity.example.akturankpredictor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var aktu_btech:LinearLayout
    lateinit var aktu_cuet:LinearLayout
    lateinit var hbtu_btech:LinearLayout
    lateinit var feedback: RelativeLayout
    lateinit var about_us: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aktu_btech = findViewById(R.id.aktu_btech)
        aktu_cuet = findViewById(R.id.aktu_cuet)
        hbtu_btech = findViewById(R.id.hbtu_btech)
        feedback = findViewById(R.id.report_feedback)
        about_us = findViewById(R.id.about_us)

        feedback.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:talktoamitmaity@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT,"Amit")
            startActivity(intent)
        }

        about_us.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/maityamit"))
            startActivity(browserIntent)
        }


        aktu_btech.setOnClickListener{
            var intent = Intent(this, SelectRankActivity::class.java)
            intent.putExtra("key","aktu_btech.json")
            startActivity(intent)
        }

        aktu_cuet.setOnClickListener{
            var intent = Intent(this, SelectRankActivity::class.java)
            intent.putExtra("key","aktu_cuet.json")
            startActivity(intent)
        }

        hbtu_btech.setOnClickListener{
            var intent = Intent(this, SelectRankActivity::class.java)
            intent.putExtra("key","hbtu_btech.json")
            startActivity(intent)
        }

    }

}