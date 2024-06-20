package com.amtech.baseetcustomer.retrofit

import com.amtech.baseetcustomer.AddService.Model.ModelPlaceOrder
import com.amtech.baseetcustomer.AddService.Model.ModelRequest
import com.amtech.baseetcustomer.Login.model.ModelLogin
import com.amtech.baseetcustomer.MainActivity.Model.ModelAllOrder
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetProfile
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslator
import com.amtech.baseetcustomer.Profile.Model.ModelUpdateProfile
import com.amtech.baseetcustomer.SignUp.Model.ModelSignUp
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiInterface {

    @POST("auth/register")
    fun register(
        @Query("f_name") f_name: String,
        @Query("l_name") l_name: String,
        @Query("email") email: String,
        @Query("phone") phone: String,
        @Query("password") password: String,
    ): Call<ModelSignUp>

    @POST("auth/login")
    fun login(
        @Query("phone") email: String,
        @Query("password") password: String,
        @Query("device_token") device_token: String,
        @Query("device_type") device_type: String,
    ): Call<ModelLogin>

    @GET("customer/info")
    fun getProfile(
        @Header("Authorization") authorization: String
    ): Call<ModelGetProfile>

    @Multipart
    @POST("customer/update-profile")
    fun updateProfile(
        @Header("Authorization") authorization: String,
        @Query("f_name") f_name:String,
        @Query("l_name") l_name:String,
        @Query("email") email:String,
        @Part image: MultipartBody.Part,
        ): Call<ModelUpdateProfile>

    @GET("customer/get_srequsts")
    fun getRequest(
        @Header("Authorization") authorization: String
    ): Call<ModelGetTranslator>

    @POST("customer/order/make_order")
    fun makeOrder(
        @Header("Authorization") authorization: String,
        @Query("food_id") food_id: String,
        @Query("user_id") user_id: String,
        @Query("request_id") request_id: String,
        @Query("payment_method") payment_method: String,
        @Query("order_type") order_type: String,
        @Query("order_payment") order_payment: String,
        @Query("pay_type") pay_type: String,
        @Query("payment_status") payment_status: String,
        @Query("order_amount") order_amount: String,
//        @Query("order_time") order_time: String,
//        @Query("coupon_discount_amount") coupon_discount_amount: String,
//        @Query("coupon_discount_title") coupon_discount_title: String,
//        @Query("coupon_code") coupon_code: String,
//        @Query("order_note") order_note: String,
//        @Query("distance") distance: String,
//        @Query("schedule_at") schedule_at: String,
//        @Query("discount_amount") discount_amount: String,
//        @Query("tax_amount") tax_amount: String,
//        @Query("address") address: String,
//        @Query("latitude") latitude: String,
//        @Query("longitude") longitude: String,
//        @Query("contact_person_name") contact_person_name: String,
//        @Query("contact_person_number") contact_person_number: String,
//        @Query("address_type") address_type: String,
//        @Query("road") road: String,
//        @Query("house") house: String,
//        @Query("floor") floor: String,
//        @Query("dm_tips") dm_tips: String,
    ): Call<ModelPlaceOrder>

    //
//    @GET("vendor/dashboard-data")
//    fun dashboard(
//        @Header("Authorization") authorization: String
//    ): Call<ModelDashboard>
//
    @Multipart
    @POST("customer/get_translator")
    fun requestTra(
        @Header("Authorization") authorization: String,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("type") type: String,
        @Query("tr_from") tr_from: String,
        @Query("tr_to") tr_to: String,
        @Query("serv_hour") serv_hour: String,
        @Query("serv_date") serv_date: String,
        @Query("price") price: String,
        @Query("start_time") start_time: String,
        @Query("end_time") end_time: String,
        @Query("country") country: String,
        @Query("food_type") food_type: String,
        @Part document: MultipartBody.Part,
    ): Call<ModelRequest>

    @POST("customer/get_translator")
    fun requestTraWithoutImage(
        @Header("Authorization") authorization: String,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("type") type: String,
        @Query("tr_from") tr_from: String,
        @Query("tr_to") tr_to: String,
        @Query("serv_hour") serv_hour: String,
        @Query("serv_date") serv_date: String,
        @Query("price") price: String,
        @Query("start_time") start_time: String,
        @Query("end_time") end_time: String,
        @Query("country") country: String,
        @Query("food_type") food_type: String,
    ): Call<ModelRequest>

    @POST("customer/get_translator")
    fun requestCar(
        @Header("Authorization") authorization: String,
        @Query("name") name: String,
        @Query("comment") comment: String,
        @Query("car_type") car_type: String,
        @Query("driv_type") driv_type: String,
        @Query("trperson") trperson: String,
        @Query("serv_date") serv_date: String,
        @Query("price") price: String,
        @Query("start_time") start_time: String,
        @Query("end_time") end_time: String,
        @Query("country") country: String,
        @Query("food_type") food_type: String,
    ): Call<ModelRequest>

    @POST("customer/get_translator")
    fun requestHome(
        @Header("Authorization") authorization: String,
        @Query("name") name: String,
        @Query("details") details: String,
        @Query("adult") adult: String,
        @Query("child") child: String,
        @Query("hometype") hometype: String,
        @Query("description") description: String,
        @Query("serv_date") serv_date: String,
        @Query("price") price: String,
        @Query("start_time") start_time: String,
        @Query("end_time") end_time: String,
        @Query("country") country: String,
        @Query("food_type") food_type: String,
    ): Call<ModelRequest>

    @GET("customer/order/list")
    fun orderList(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: String,
        @Query("offset") offset: String,
     ): Call<ModelAllOrder>


}