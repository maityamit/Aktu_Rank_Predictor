package akturankpredictorbyamitmaity.example.akturankpredictor

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ChatActivity : AppCompatActivity() {

    lateinit var editText: EditText
    private lateinit var progressDialog: ProgressDialog
    lateinit var imageView: ImageView

    lateinit var recyclerView: RecyclerView
    lateinit var rootRef: DatabaseReference
    var chatList = ArrayList<Pair<Pair<String,String>,String>>()
    var selfUserID:String = ""
    lateinit var chatviewLayout: LinearLayout
    lateinit var no_queries_yet:CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Loading....")

        no_queries_yet = findViewById(R.id.no_queries_yet)

        chatviewLayout = findViewById(R.id.chatViewlinear)
        recyclerView = findViewById(R.id.chatViewRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        editText = findViewById(R.id.user_chat_edittext)
        imageView = findViewById(R.id.user_chat_sendButton)


        val sharedPreferences = getSharedPreferences("userDataStoreLocal", MODE_PRIVATE)
        val userName = sharedPreferences?.getString("userID","")

        if(!userName.equals("")){


            no_queries_yet.visibility = View.GONE

            chatviewLayout.visibility = View.GONE
            if(userName!=null) chatList.add(Pair(Pair("selfUserID","mySelfAMIT"),userName))
            chatList.add(Pair(Pair("mySelfAMIT","selfUserID"),"\uD83C\uDF93 Welcome to College Predictor! \uD83C\uDF1F"))
            chatList.add(Pair(Pair("mySelfAMIT","selfUserID"),"Need admission help? Fill our form, our expert counselors will WhatsApp you and provide free chat support throughout the college admission process:\n https://forms.gle/GDgZUTPVmD2vj7RP6"))
            chatList.add(Pair(Pair("mySelfAMIT","selfUserID"),"Join our lively Telegram group with college seniors and future students! \uD83D\uDE80 : https://t.me/college_counseling_assistance"))
            chatList.add(Pair(Pair("mySelfAMIT","selfUserID"),"Thanks You !!"))

            val chatAdapter:ChatAdapter = ChatAdapter(this@ChatActivity,chatList)
            recyclerView.adapter = chatAdapter
            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1)


        }



        imageView.setOnClickListener{
            val str:String = editText.text.toString()
            if(str.equals("")){
                Toast.makeText(applicationContext,"Enter Chat", Toast.LENGTH_SHORT).show()
            }else{
                progressDialog.show()
                chatList.add(Pair(Pair("selfUserID","mySelfAMIT"),str))
                chatList.add(Pair(Pair("mySelfAMIT","selfUserID"),"\uD83C\uDF93 Welcome to College Predictor! \uD83C\uDF1F"))
                chatList.add(Pair(Pair("mySelfAMIT","selfUserID"),"Need admission help? Fill our form, our expert counselors will WhatsApp you and provide free chat support throughout the college admission process: https://forms.gle/GDgZUTPVmD2vj7RP6"))
                chatList.add(Pair(Pair("mySelfAMIT","selfUserID"),"Join our lively Telegram group with college seniors and future students! \uD83D\uDE80 : https://t.me/college_counseling_assistance"))
                chatList.add(Pair(Pair("mySelfAMIT","selfUserID"),"Thanks You !!"))

                val chatAdapter:ChatAdapter = ChatAdapter(this@ChatActivity,chatList)
                recyclerView.adapter = chatAdapter
                recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1)
                progressDialog.dismiss()
                editText.text = null

                val myEdit = sharedPreferences.edit()
                myEdit.putString("userID",str)
                myEdit.apply()

                no_queries_yet.visibility = View.GONE

                chatviewLayout.visibility = View.GONE


            }
        }


    }

    override fun onStart() {
        super.onStart()


    }

}