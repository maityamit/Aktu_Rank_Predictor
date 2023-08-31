package akturankpredictorbyamitmaity.example.akturankpredictor

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var aktu_btech:LinearLayout
    lateinit var aktu_cuet:LinearLayout
    lateinit var hbtu_btech:LinearLayout
    lateinit var feedback: RelativeLayout
    lateinit var about_us: RelativeLayout
    lateinit var counselling:RelativeLayout
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        aktu_btech = findViewById(R.id.aktu_btech)
        aktu_cuet = findViewById(R.id.aktu_cuet)
        hbtu_btech = findViewById(R.id.hbtu_btech)
        feedback = findViewById(R.id.report_feedback)
        about_us = findViewById(R.id.about_us)
        counselling = findViewById(R.id.counselling_assistant)


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_NUMBERS
            ) ==
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission check

            // Create obj of TelephonyManager and ask for current telephone service
            val telephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val phoneNumber = telephonyManager.line1Number
            Toast.makeText(applicationContext,phoneNumber.toString(),Toast.LENGTH_LONG).show()
            if(phoneNumber.toString() != ""){
                database = Firebase.database.reference
                database.child("user_mobile_no").child(phoneNumber).setValue("true")
            }
        } else {
            // Ask for permission
            requestPermission()
        }


        feedback.setOnClickListener{
            showReportSheet(it)
        }

        about_us.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/maityamit"))
            startActivity(browserIntent)
        }
        counselling.setOnClickListener{
            val inTent = Intent(Intent.ACTION_VIEW,Uri.parse("https://forms.gle/3ZtmUBnaaPg9QaRe7"))
            startActivity(inTent)
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

    private fun showReportSheet(view:View){
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(view.context)

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.report_feedback_layout, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        val btnSignIN = view.findViewById<Button>(R.id.report_feedback_button)
        val editText = view.findViewById<EditText>(R.id.text_feedback_)
        val emailID = view.findViewById<EditText>(R.id.email_adress_report)

       btnSignIN.setOnClickListener{
           val strText = editText.text.toString()
           val strEmail = emailID.text.toString()
           if(!strText.equals("") && !strEmail.equals("")){

               val user = ReportFeedbackClass(strText.toString(),strEmail.toString())
               database = Firebase.database.reference
               database.child("feedback_report").push().setValue(user).addOnCompleteListener(){task->
                   if(task.isSuccessful){
                       Toast.makeText(applicationContext,"Done",Toast.LENGTH_LONG).show()
                       dialog.dismiss()
                   }else{
                       Toast.makeText(applicationContext,"Sorry! There's an issue.",Toast.LENGTH_LONG).show()
                   }
               }

           }else{
               Toast.makeText(applicationContext,"Sorry, Please fill properly",Toast.LENGTH_LONG).show()
           }

        }

        dialog.setContentView(view)

        dialog.show()
    }


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.READ_PHONE_STATE
                ), 100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                val telephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_PHONE_NUMBERS
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                val phoneNumber = telephonyManager.line1Number
                if(phoneNumber.toString() != ""){
                    database = Firebase.database.reference
                    database.child("user_mobile_no").child(phoneNumber).setValue("true")
                }
            }
            else -> throw IllegalStateException("Unexpected value: $requestCode")
        }
    }

    data class ReportFeedbackClass(val userText: String, val email: String) {}


}