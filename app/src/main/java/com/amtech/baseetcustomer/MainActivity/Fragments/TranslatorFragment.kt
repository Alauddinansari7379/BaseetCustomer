package com.amtech.baseetcustomer.MainActivity.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.amtech.baseetcustomer.AddService.Translator
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterCar
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterTranslator
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.back
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.car
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.home
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.statisticsList
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.translator
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetProfile
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslator
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslatorItem
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentTranslatorBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL

class TranslatorFragment : Fragment() {
    private lateinit var binding: FragmentTranslatorBinding
    private lateinit var sessionManager: SessionManager
    var count = 0
    var mainData = ArrayList<ModelGetTranslatorItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_translator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTranslatorBinding.bind(view)
        sessionManager = SessionManager(requireActivity())
        activity?.let { MainActivity().languageSetting(it,sessionManager.selectedLanguage.toString()) }

        Log.e(" sessionManager.idToken", sessionManager.idToken.toString())
        with(binding) {
            apiCallGetRequest()

            if (sessionManager.customerName!!.isNotEmpty()){
                apiCallGetProfile()
            }

            btnRequest.setOnClickListener {
                startActivity(Intent(requireActivity(), Translator::class.java))
            }

            binding.recyclerView.apply {
                adapter = AdapterTranslator(requireActivity(), mainData)
            }

            spinnerStatistics.adapter = ArrayAdapter<ModelSpinner>(
                requireActivity(), R.layout.spinner_layout, statisticsList
            )

            spinnerStatistics.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?, view: View?, i: Int, l: Long
                    ) {
                        if (statisticsList.size > 0) {
                            val statusChange = statisticsList[i].value

                            val requested = ArrayList<ModelGetTranslatorItem>()
                            val pending = ArrayList<ModelGetTranslatorItem>()
                            for (i in translator) {
                                val jsonArray = JsonArray()
                                jsonArray.add("0")
                                val jsonObject = JsonObject()
                                jsonObject.add("accept_by", jsonArray)

                                val jsonString = "${i.accept_by}"

                                println(jsonString)
                                Log.e("accept_by",jsonString)
                                if (jsonString.contains("0")) {
                                    requested.add(i)
                                } else {
                                    pending.add(i)
                                }

                            }
                            when (statusChange){
                               "All Booking"-> {
                                   binding.recyclerView.apply {
                                       adapter = AdapterTranslator(requireActivity(), translator)
                                   }
                               }
                                "Pending"->{
                                    binding.recyclerView.apply {
                                        adapter = AdapterTranslator(requireActivity(), pending)
                                    }
                                }else->{
                                binding.recyclerView.apply {
                                    adapter = AdapterTranslator(requireActivity(), requested)
                                }
                                }

                            }
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }
        }


    }


    private fun apiCallGetRequest() {
        AppProgressBar.showLoaderDialog(requireActivity())
        AppProgressBar.showLoaderDialog(requireActivity())
        ApiClient.apiService.getRequest(
            sessionManager.idToken.toString(),
        )
            .enqueue(object : Callback<ModelGetTranslator> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetTranslator>, response: Response<ModelGetTranslator>
                ) {
                    try {
                        if (response.code() == 404) {
                            activity?.let { myToast(it, resources.getString(R.string.Something_went_wrong)) }
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 500) {
                            activity?.let { myToast(it, resources.getString(R.string.Server_Error)) }
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.isEmpty()) {
                            activity?.let { myToast(it, resources.getString(R.string.No_Data_Found)) }
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!
                            car.clear()
                            home.clear()
                            translator.clear()

                            for (i in mainData) {
                                if (i.food_type != null) {
                                    if (i.status.toString()!="2") {
                                        when (i.food_type) {
                                            "car" -> car.add(i)
                                            "home" -> home.add(i)
                                            "translator" -> translator.add(i)
                                        }
                                    }
                                    car.reverse()
                                    home.reverse()
                                    translator.reverse()
                                }

                            }
                            Log.e("mainData.size", mainData.size.toString())
                            Log.e("car.size", car.size.toString())
                            Log.e("home.size", home.size.toString())
                            Log.e("translator.size", translator.size.toString())
                            binding.recyclerView.apply {
                                adapter = AdapterTranslator(requireActivity(), translator)
                            }

                            val recyclerViewCar =
                                (requireActivity().findViewById<View>(R.id.recyclerViewCar) as RecyclerView)

                            recyclerViewCar.apply {
                                adapter = AdapterCar(requireActivity(), car)
                            }

                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        activity?.let { myToast(it,  resources.getString(R.string.Something_went_wrong)) }
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelGetTranslator>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallGetRequest()
                    } else {
                        activity?.let { myToast(it, t.message.toString()) }
                        AppProgressBar.hideLoaderDialog()
                    }
                    AppProgressBar.hideLoaderDialog()
                }
            })
    }
    override fun onResume() {
        super.onResume()
        apiCallGetRequest()
        if (back){
            back=false
            apiCallGetRequest()
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
                        activity?.let { myToast(it,resources.getString(R.string.Server_Error)) }
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 401) {
                        activity?.let { myToast(it,  resources.getString(R.string.Unauthorized)) }
                        AppProgressBar.hideLoaderDialog()

                    } else {

                        sessionManager.customerName = response.body()!!.f_name + " " + response.body()!!.l_name
                        sessionManager.phoneNumber = response.body()!!.phone
                        sessionManager.email = response.body()!!.email
                        sessionManager.profilePic = response.body()!!.app_image

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    activity?.let { myToast(it,  resources.getString(R.string.Something_went_wrong)) }
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
                    activity?.let { myToast(it, t.message.toString()) }
                    AppProgressBar.hideLoaderDialog()

                }
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

//    override fun videoCall(toString: String) {
//        val jitsiMeetUserInfo = JitsiMeetUserInfo()
//        jitsiMeetUserInfo.displayName = sessionManager.customerName
//        jitsiMeetUserInfo.email = sessionManager.email
//        try {
//            val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
//                .setServerURL(URL("https://jvc.ethicalhealthcare.in/"))
//                .setRoom(toString)
//                .setAudioMuted(false)
//                .setVideoMuted(true)
//                .setAudioOnly(false)
//                .setUserInfo(jitsiMeetUserInfo)
//                .setConfigOverride("enableInsecureRoomNameWarning", false)
//                .setFeatureFlag("readOnlyName", true)
//                .setFeatureFlag("prejoinpage.enabled", false)
//                //  .setFeatureFlag("lobby-mode.enabled", false)
//                // .setToken("123") // Set the meeting password
//                //.setFeatureFlag("autoKnockLobby", false) // Disable lobby mode
//                //.setFeatureFlag("disableModeratorIndicator", false)
//                //.setFeatureFlag("chat.enabled",false)
//                .setConfigOverride("requireDisplayName", true)
//                .build()
//            JitsiMeetActivity.launch(context, defaultOptions)
//
//            //  startActivity(Intent(requireContext(),Rating::class.java))
//        } catch (e: MalformedURLException) {
//            e.printStackTrace();
//        }
//    }
}