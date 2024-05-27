package com.amtech.baseetcustomer.Helper

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.amtech.baseetcustomer.R


@SuppressLint("NonConstantResourceId")
object AppProgressBar {
    var dialog: Dialog? = null

    fun showLoaderDialog(context: Context?) {
        try {
            if (dialog != null) if (dialog!!.isShowing) dialog!!.dismiss()
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            dialog!!.setContentView(R.layout.custom_loader)

            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideLoaderDialog() {
        try {
            if (dialog != null && dialog!!.isShowing) dialog!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
