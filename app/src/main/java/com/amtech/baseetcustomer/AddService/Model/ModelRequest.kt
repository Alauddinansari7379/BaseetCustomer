package com.amtech.baseetcustomer.AddService.Model

data class ModelRequest(
    val country: String,
    val created_at: String,
    val customer_id: Int,
    val description: String,
    val document: String,
    val end_time: String,
    val food_type: String,
    val id: Int,
    val message: String,
    val name: String,
    val price: String,
    val serv_date: String,
    val serv_hour: String,
    val start_time: String,
    val tr_from: String,
    val tr_to: String,
    val type: String,
    val updated_at: String
)