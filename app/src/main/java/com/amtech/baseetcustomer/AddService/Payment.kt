package com.amtech.baseetcustomer.AddService

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amtech.baseetcustomer.AddService.modelcurrency.ModelConvertCurrency
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityPaymentBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class Payment : AppCompatActivity() {
    private val binding by lazy { ActivityPaymentBinding.inflate(layoutInflater) }
    private val context = this@Payment
    private lateinit var sessionManager: SessionManager
    private val timer = Timer()
    var price = 0.0
    var priceNew = ""
    var currency = ""
    var count = 0
    var callFrom = ""
    var serviceDate = ""
    var hour = ""
    var foodId = ""
    var orderid = ""
    var id = ""
    var paymentType = ""
    var done = false
    var priceNewPar: Double? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
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

//102035753236
        callFrom = intent!!.getStringExtra("callFrom").toString()
        price = intent!!.getStringExtra("price")!!.toFloat().toDouble()
        price = BigDecimal(price).setScale(2, RoundingMode.HALF_UP).toDouble()

        foodId = intent!!.getStringExtra("foodId").toString()
        orderid = intent!!.getStringExtra("order_id").toString()
        id = intent!!.getStringExtra("id").toString()
        serviceDate = intent!!.getStringExtra("serviceDate").toString()
        //   price.substringAfter(".")

//         val regex = """^\d+""".toRegex()
//        val matchResult = regex.find(price.toString())
//        val integerString = matchResult?.value ?: "0"
        //  val integerNumber = integerString.toDouble()
        apiCallGetCurrencyConvertion()
        val integerNumber = price

        MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())

//        if (MainActivity.refreshLanNew) {
//            MainActivity.refreshLanNew = false
//            refresh()
//        }
        binding.tvAmount.text =
            resources.getString(R.string.Pay_Full_Payment_USD) + integerNumber
        try {
            if (callFrom == "Remaining") {
                priceNew = integerNumber.toString()
                binding.tvAmount.text =
                    resources.getString(R.string.Pay_Remaining_Payment_USD) + integerNumber
                paymentType = "full_payment"
                binding.layoutPar.visibility = View.GONE
                binding.radioFull.isChecked = true
            } else {
                if (serviceDate != "NA" && serviceDate != "") {
                    val currentDate: String =
                        SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(Date())
                    val serviceDateNew = serviceDate.substringBefore(",").replace(",", "")
                    //05-14-2024
                    calculateHours(currentDate, serviceDateNew)

                    //  val montCheck=serviceDate.substring(3,5)
                    try {
                        if (hour.toInt() > 48) {
                            priceNewPar = (integerNumber * 30) / 100

                            binding.tvAmountPar.text =
                                resources.getString(R.string.Pay_Partia_Payment_USD) + priceNewPar
                            paymentType = "partial"
                            this.priceNew = priceNewPar!!.toDouble().toString()

                        } else {
                            binding.layoutPar.visibility = View.GONE
                            binding.radioFull.isChecked = true

                            priceNew = integerNumber.toString()
                            binding.tvAmount.text =
                                resources.getString(R.string.Pay_Full_Payment_USD) + integerNumber
                            paymentType = "full_payment"
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    priceNew = integerNumber.toString()
                    binding.tvAmount.text =
                        resources.getString(R.string.Pay_Full_Payment_USD) + integerNumber
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }




        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            image.setOnClickListener {
                openImageChooser()
            }
            switchPayment.isChecked = true
            switchOnline.setOnCheckedChangeListener { _, isChecked ->
                if (switchBankT.isChecked) {
                    switchBankT.isChecked = false
                }
                if (switchWesternM.isChecked) {
                    switchWesternM.isChecked = false
                }
                if (isChecked) {
                    layoutImage.visibility = View.GONE
                }
            }

            switchBankT.setOnCheckedChangeListener { _, isChecked ->
                if (switchOnline.isChecked) {
                    switchOnline.isChecked = false
                }
                if (switchWesternM.isChecked) {
                    switchWesternM.isChecked = false
                }
                if (isChecked) {
                    layoutImage.visibility = View.VISIBLE
                } else {
                    layoutImage.visibility = View.GONE
                }
            }

            switchWesternM.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    layoutImage.visibility = View.VISIBLE
                } else {
                    layoutImage.visibility = View.GONE
                }
                if (switchOnline.isChecked) {
                    switchOnline.isChecked = false
                }
                if (switchBankT.isChecked) {
                    switchBankT.isChecked = false
                }
            }


            radioPar.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    radioFull.isChecked = false
                    paymentType = "partial"
                    priceNew = priceNewPar.toString()

                } else {
                    // Do something when the radio button is deselected (if needed)
                }
            }

            radioFull.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    radioPar.isChecked = false
                    paymentType = "full_payment"
                    priceNew = price.toString()

                } else {
                    // Do something when the radio button is deselected (if needed)
                }
            }


            btnOrderNow.setOnClickListener {
                if (!radioPar.isChecked && !radioFull.isChecked) {
                    myToast(context, "Please select type")
                    return@setOnClickListener
                }

                if (switchOnline.isChecked) {
                    layoutPayment.visibility = View.GONE
                    webView.visibility = View.VISIBLE
                    // Enable JavaScript (if needed)
                    // Set a WebViewClient to handle loading of pages within the WebView
                    webView.webViewClient = WebViewClient()
                    CookieManager.getInstance().removeAllCookies(null)
                    CookieManager.getInstance().flush()
                    webView.clearCache(true)
                    webView.clearHistory()


                    val URL =
                        "https://baseet.thedemostore.in/sadabpaynew.php?email=abcs@gmail.com&mobile=9981482211&quantity=1&amount=${10}"

                    // Load a URL
                    // webView.loadUrl("https://baseet.thedemostore.in/sadabpaynew.php?email=abcs@gmail.com&mobile=9981482211&quantity=1&amount=10")

                    webView.settings.javaScriptEnabled = true

                    // Enable Dom storage (if needed)
                    webView.settings.domStorageEnabled = true
                    WebView.setWebContentsDebuggingEnabled(true)


//            webView.webViewClient = object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                    view?.loadUrl(URL!!)
//                    return true
//                }
//
//                override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//                    handler?.proceed() // Ignore SSL certificate errors
//                    Log.d("webVIewError",error.toString())
//                }
//            }
                    webView.addJavascriptInterface(WebAppInterface(this@Payment), "Android")
                    val i = Intent(context, MainActivity::class.java)
                        .putExtra("callFrom", "Payment")
                        .putExtra("id", id)
                        .putExtra("foodId", foodId)
                        .putExtra("currency", currency)
                        .putExtra("paymentType", paymentType)
                        .putExtra("text", "text")
                        .putExtra("priceNew", priceNew)
                    context.startActivity(i)
                    finish()
                    // Load the URL
                    webView.loadUrl("https://baseet.thedemostore.in/sadabpaynew.php?email=abcs@gmail.com&mobile=9981482211&quantity=1&amount=${priceNew}")
                } else {
                    myToast(context, resources.getString(R.string.Please_Select_Payment_Type))
                }
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            // Call your method here
                            webView.evaluateJavascript(
                                "(function() { return document.body.innerText; })();"
                            ) { text ->
                                copyToClipboard(text)
//                                val regex = """\[STATUS\] => (\w+)""".toRegex()
//                                val matchResult = regex.find(text)
                                if (text.contains("Array ( [website_ref_no]")) {
                                    done = true
                                    if (done) {
                                        done = false
                                        timer.cancel()
                                        // val statusValue = matchResult.groupValues[1]
                                        val i = Intent(context, MainActivity::class.java)
                                            .putExtra("callFrom", "Payment")
                                            .putExtra("id", id)
                                            .putExtra("foodId", foodId)
                                            .putExtra("paymentType", paymentType)
                                            .putExtra("text", text)
                                            .putExtra("priceNew", priceNew)
                                        context.startActivity(i)
                                        finish()
                                    }
                                }
                            }
                        }
                    }
                }, 0, 1000)
            }


//            btnDone.setOnClickListener {
//
//                webView.evaluateJavascript(
//                    "(function() { return document.body.innerText; })();"
//                ) { text ->
//                    copyToClipboard(text)
//                    val i = Intent(context, MainActivity::class.java)
//                        .putExtra("callFrom", "Payment")
//                        .putExtra("text", text)
//                    context.startActivity(i)
//                  //  Toast.makeText(this@Payment, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
//                }

        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateHours(startDateString: String, endDateString: String) {

        try {
            // Define the date format
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

            // Parse the start and end date strings to LocalDateTime
            val startDateTime = LocalDateTime.parse("${startDateString} 00:00:00", formatter)
            val endDateTime = LocalDateTime.parse("${endDateString} 00:00:00", formatter)

            // Calculate the duration between the two dates in hours
            hour = ChronoUnit.HOURS.between(startDateTime, endDateTime).toString()
            Log.d("Hour", ChronoUnit.HOURS.between(startDateTime, endDateTime).toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //Card number: 4215 3755 0088 3243

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            (MediaStore.ACTION_IMAGE_CAPTURE)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, Translator.REQUEST_CODE_IMAGE)

//        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
//        pdfIntent.type = "application/pdf"
//        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
//        startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

        }
    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.refreshLanNew = true
        timer.cancel()

    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("webview_text", text)
        clipboard.setPrimaryClip(clip)
    }

    fun apiCallGetCurrencyConvertion() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getCurrencyConversion(sessionManager.idToken.toString(), price.toString())
            .enqueue(object : Callback<ModelConvertCurrency> {
                override fun onResponse(
                    call: Call<ModelConvertCurrency>,
                    response: Response<ModelConvertCurrency>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            count = 0
                            priceNew = response.body()!!.converted_price.toString()
                            currency = response.body()!!.currency.toString()
                            AppProgressBar.hideLoaderDialog()
                        }

                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelConvertCurrency>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        apiCallGetCurrencyConvertion()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })
    }
}


class WebAppInterface(private val context: Payment) {

    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}