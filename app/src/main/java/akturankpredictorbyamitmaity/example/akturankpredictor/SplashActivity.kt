package akturankpredictorbyamitmaity.example.akturankpredictor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView

class SplashActivity : AppCompatActivity() {

    lateinit var textview:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        textview = findViewById<TextView>(R.id.splash_activity_text)
        textview.visibility = View.VISIBLE
        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        textview.startAnimation(animationFadeIn)

        sendUserToLoginActivity()

    }

    private fun sendUserToLoginActivity() {
        Handler().postDelayed({
            val loginIntent = Intent(applicationContext, MainActivity::class.java)
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(loginIntent)
            finish()
        }, 2500)
    }

}