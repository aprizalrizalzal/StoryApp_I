package me.zal.rizal.aprizal.storyapp.addition

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import me.zal.rizal.aprizal.storyapp.R

class CustomProgressDialog(private val activity: Activity) {
    private lateinit var progressDialog: Dialog

    fun showProgressDialog() {
        progressDialog = Dialog(activity)
        progressDialog.setContentView(R.layout.progress_bar)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setCancelable(true)
        progressDialog.create()
        progressDialog.show()
    }

    fun dismissProgressDialog() {
        progressDialog.dismiss()

    }
}