package com.amtech.baseetcustomer.MainActivity.Model

data class ModelAllOrder(
    val limit: String,
    val offset: String,
    val orders: ArrayList<Order>,
    val total_size: Int
)