package com.amtech.baseetcustomer.Profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.ImageUploadClass.UploadRequestBody
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.Profile.Model.ModelUpdateProfile
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityEditProfileBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class EditProfile : AppCompatActivity(), UploadRequestBody.UploadCallback  {
    private val binding by lazy { ActivityEditProfileBinding.inflate(layoutInflater) }
    private val context = this@EditProfile
    private var selectedImageUri: Uri? = null
     lateinit var sessionManager: SessionManager
     var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager=SessionManager(context)
        MainActivity().languageSetting(context,sessionManager.selectedLanguage.toString())
        if (sessionManager.selectedLanguage == "en") {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.arabic_text)
        } else {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.english_text)
        }

        binding.imgLan.setOnClickListener {
            if (sessionManager.selectedLanguage == "en") {
                sessionManager.selectedLanguage = "ar"
                MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())
                overridePendingTransition(0, 0)
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)
            } else {
                sessionManager.selectedLanguage = "en"
                MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())
                overridePendingTransition(0, 0)
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
        with(binding) {
            if (sessionManager.customerName!!.isNotEmpty()){
                tvName.text=sessionManager.customerName.toString()
                tvEmail.text=sessionManager.email.toString()
            }
            imgBack.setOnClickListener {
                onBackPressed()
            }
            imgProfile.setOnClickListener {
                openImageChooser()
            }
            btnEdit.setOnClickListener {
                if (edtFirstName.text!!.isEmpty()) {
                    edtFirstName.error = resources.getString(R.string.Enter_First_Name)
                     edtFirstName.requestFocus()
                    return@setOnClickListener
                }
                if (edtLastName.text!!.isEmpty()) {
                    edtLastName.error = resources.getString(R.string.Enter_Last_Name)
                    edtLastName.requestFocus()
                    return@setOnClickListener
                }

                if (edtEmail.text!!.isEmpty()) {
                    edtEmail.error = resources.getString(R.string.Enter_Email)
                    edtEmail.requestFocus()
                    return@setOnClickListener
                }

                apiCallUpdateProfile()

            }

        }
    }
    private fun apiCallUpdateProfile() {
        if (selectedImageUri == null) {
            myToast(context, resources.getString(R.string.Select_an_Image_First))
            return
        }
        AppProgressBar.showLoaderDialog(context)
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file, "image", this)
        ApiClient.apiService.updateProfile(
            sessionManager.idToken.toString(),
          "","",
            "translator",
            MultipartBody.Part.createFormData("document", file.name, body),
        ).enqueue(object : Callback<ModelUpdateProfile> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelUpdateProfile>, response: Response<ModelUpdateProfile>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        onBackPressed()

                    } else {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, resources.getString(R.string.Something_went_wrong))
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelUpdateProfile>, t: Throwable) {
                count++
                if (count<= 3) {
                    Log.e("count", count.toString())
                    apiCallUpdateProfile()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
                myToast(context, resources.getString(R.string.Something_went_wrong))
                AppProgressBar.hideLoaderDialog()
            }

        })

    }
    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            (MediaStore.ACTION_IMAGE_CAPTURE)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)
//
//        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
//        pdfIntent.type = "application/pdf"
//        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
//        startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    Log.e("data?.data", data?.data.toString())
//                    binding!!.tvChoice.setTextColor(Color.parseColor("#FF4CAF50"))
//                    binding!!.tvChoice.text = "Image Selected"

                    //binding.imageViewNew.visibility = View.VISIBLE
                    binding.imgProfile.setImageURI(selectedImageUri)
                }
            }
        }
    }
     companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    override fun onProgressUpdate(percentage: Int) {

    }
}

