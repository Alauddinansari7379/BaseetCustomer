package com.amtech.baseetcustomer.MainActivity.Fragments

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amtech.baseetcustomer.AddService.CarRental
import com.amtech.baseetcustomer.AddService.HomeRental
import com.amtech.baseetcustomer.AddService.Translator
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentHomeBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var  binding: FragmentHomeBinding
    lateinit var sessionManager: SessionManager
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentHomeBinding.bind(view)
        sessionManager=SessionManager(activity)
        activity?.let { MainActivity().languageSetting(it,sessionManager.selectedLanguage.toString()) }


        with(binding){
             cardHome.setOnClickListener {
                startActivity(Intent(requireActivity(),HomeRental::class.java))
            }
            cardTra.setOnClickListener {
                startActivity(Intent(requireActivity(),Translator::class.java))
            }
            cardCar.setOnClickListener {
                startActivity(Intent(requireActivity(),CarRental::class.java))
            }
            binding.imgLan.setOnClickListener {
                languageDialog()
            }
        }
    }

    private fun languageDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_langauge, null)
        dialog = activity?.let { Dialog(it) }



        val radioEnglish = view!!.findViewById<RadioButton>(R.id.radioEnglish)
        val radioArabic = view!!.findViewById<RadioButton>(R.id.radioArabic)

        if (sessionManager.selectedLanguage=="ar"){
            radioArabic.isChecked=true
        }else{
            radioEnglish.isChecked=true
        }

        radioEnglish.setOnCheckedChangeListener { _, _ ->
            val languageToLoad = "en"
            val locale: Locale = Locale(languageToLoad)
            Locale.setDefault(locale)
            val config: Configuration = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            sessionManager.selectedLanguage = "en"
            dialog?.dismiss()
            (activity as MainActivity).refreshNew()


        }
        radioArabic.setOnCheckedChangeListener { _, _ ->
            val languageToLoad = "ar"
            val locale: Locale = Locale(languageToLoad)
            Locale.setDefault(locale)
            val config: Configuration = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            sessionManager.selectedLanguage = "ar"

            dialog?.dismiss()
            (activity as MainActivity).refreshNew()
        }

        dialog = activity?.let { Dialog(it) }
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)

        dialog?.show()
    }

}