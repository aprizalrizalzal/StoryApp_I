package me.zal.rizal.aprizal.storyapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import me.zal.rizal.aprizal.storyapp.main.StoriesActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : Activity() {
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
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(Intent(applicationContext, StoriesActivity::class.java))
                finish()
            }
        }
        thread.start()
    }
}