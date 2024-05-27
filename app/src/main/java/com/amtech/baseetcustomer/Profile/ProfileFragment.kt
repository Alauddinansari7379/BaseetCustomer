package com.amtech.baseetcustomer.Profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.Login.Login
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetProfile
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentProfileBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sessionManager: SessionManager
    var count = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        if (!sessionManager.idToken.isNullOrEmpty()){
            apiCallGetProfile()
        }else{
            binding.tvName.text = sessionManager.customerName
            binding.tvEmail.text =sessionManager.email

//            if (sessionManager.profilePic != null) {
//                Picasso.get().load(sessionManager.profilePic)
//                    .placeholder(R.drawable.usernew)
//                    .error(R.drawable.usernew)
//                    .into(binding.imgProfile)
//
//            }
        }
        with(binding) {
            layoutEdit.setOnClickListener {
                startActivity(Intent(requireContext(), EditProfile::class.java))
            }

            cardChange.setOnClickListener {
                startActivity(Intent(requireContext(), ChangePassword::class.java))
            }

            cardLogout.setOnClickListener {
                SweetAlertDialog(
                    requireContext(),
                    SweetAlertDialog.WARNING_TYPE
                ).setTitleText("Are you sure want to Logout?").setCancelText("No")
                    .setConfirmText("Yes").showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()
                        sessionManager.logout()
                        val intent = Intent(requireContext(), Login::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }.setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }.show()


            }

        }

    }

    private fun apiCallGetProfile() {

        ApiClient.apiService.getProfile(
            sessionManager.idToken.toString()
        ).enqueue(object :
            Callback<ModelGetProfile> {
            @SuppressLint("LogNotTimber", "LongLogTag", "SetTextI18n")
            override fun onResponse(
                call: Call<ModelGetProfile>,
                response: Response<ModelGetProfile>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(requireActivity(), "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 401) {
                        myToast(requireActivity(), "Unauthorized")
                        AppProgressBar.hideLoaderDialog()

                    } else {

                        sessionManager.customerName =
                            response.body()!!.f_name + " " + response.body()!!.l_name
                        sessionManager.phoneNumber = response.body()!!.phone
                        sessionManager.email = response.body()!!.email
                        sessionManager.profilePic = response.body()!!.app_image

                        binding.tvName.text =
                            response.body()!!.f_name + " " + response.body()!!.l_name
                        binding.tvEmail.text = response.body()!!.email

//                        if (response.body()!!.app_image != null) {
//                            Picasso.get().load("https:" +response.body()!!.app_image)
//                                .placeholder(R.drawable.usernew)
//                                .error(R.drawable.usernew)
//                                .into(binding.imgProfile)
//
//                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(requireActivity(), "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelGetProfile>, t: Throwable) {
                AppProgressBar.hideLoaderDialog()
                count++
                if (count <= 3) {
                    Log.e("count", count.toString())
                    apiCallGetProfile()
                } else {
                    myToast(requireActivity(), t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

}