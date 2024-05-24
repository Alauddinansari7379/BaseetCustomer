package com.amtech.baseetcustomer.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.Login.Login
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentProfileBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager

class ProfileFragment : Fragment() {
    private lateinit var  binding:FragmentProfileBinding
    private lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentProfileBinding.bind(view)
        sessionManager=SessionManager(requireContext())
        with(binding){
            layoutEdit.setOnClickListener {
                startActivity(Intent(requireContext(),EditProfile::class.java))
            }

            cardChange.setOnClickListener {
                startActivity(Intent(requireContext(),ChangePassword::class.java))
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
}