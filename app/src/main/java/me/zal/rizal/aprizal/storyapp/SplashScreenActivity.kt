package me.zal.rizal.aprizal.storyapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.zal.rizal.aprizal.storyapp.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val thread = Thread {
            val timeSplash: Long = 1000
            try {
                Thread.sleep(timeSplash)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }
        thread.start()
    }
}