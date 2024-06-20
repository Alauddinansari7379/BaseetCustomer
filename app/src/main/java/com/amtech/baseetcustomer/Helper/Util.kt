package com.amtech.baseetcustomer.Helper

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.amtech.baseetcustomer.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Util {
    private var dateFormat: SimpleDateFormat? = null
    private var spDatePickerPref: SharedPreferences? = null
    const val MAX_DATE_VALUE = "MAX_DATE_VALUE"
    const val MIN_DATE_VALUE = "MIN_DATE_VALUE"

    fun putSharedPrefValue(context: Context, key: String, value: String){
        if (spDatePickerPref == null) {
            spDatePickerPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        }
        val spDatePickerPrefEdit = spDatePickerPref?.edit()
        spDatePickerPrefEdit?.putString(key, value)
        spDatePickerPrefEdit?.apply()
    }

    fun getSharedPrefValue(context: Context, key: String): String? {
        if (spDatePickerPref == null) {
            spDatePickerPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        }
        return spDatePickerPref!!.getString(key, null)
    }

    fun getDate(date: Date?): String {
        dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return dateFormat!!.format(date)
    }

    @JvmStatic
    fun getDate(date: String?): Date? {
        var newDate: Date? = null
        dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        try {
            newDate = dateFormat!!.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return newDate
    }

    object LocaleHelper {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

        fun setLocale(context: Context, language: String): Context {
            persist(context, language)
            return updateLocale(context, language)
        }

        fun onAttach(context: Context): Context {
            val lang = getPersistedData(context, Locale.getDefault().language)
            return setLocale(context, lang)
        }

        private fun getPersistedData(context: Context, defaultLanguage: String): String {
            val preferences = context.getSharedPreferences("locale_prefs", Context.MODE_PRIVATE)
            return preferences.getString(SELECTED_LANGUAGE, defaultLanguage) ?: defaultLanguage
        }

        private fun persist(context: Context, language: String) {
            val preferences = context.getSharedPreferences("locale_prefs", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(SELECTED_LANGUAGE, language)
            editor.apply()
        }

        private fun updateLocale(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val config = Configuration()
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        }
    }

}