package com.amtech.baseetcustomer.MainActivity.Model

data class ModelGetTranslatorItem(
    val accept_by: ArrayList<String>,
    val child: String?,
    val country: String?,
    val created_at: String?,
    val customer_id: String?,
    val description: String?,
    val details: String?,
    val driv_type: String?,
    val end_time: String?,
    val food_type: String?,
    val from_ven_stts: String?,
    val id: Int,
    val name: String?,
    val stts: String?,
    val price: String?,
    val res_id: String?,
    val serv_date: String?,
    val serv_hour: String?,
    val serv_id: ArrayList<ServId>,
    val start_time: String?,
    val status: String?,
    val tr_from: String?,
    val tr_to: String?,
    val trperson: String?,
    val type: String?,
    val updated_at: String?,
    val ven_id: String?,
    val whchserv: String
)