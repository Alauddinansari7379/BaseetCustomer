package com.amtech.baseetcustomer.AddService.modelcurrency

data class ModelConvertCurrency(
    val converted_price: Double,
    val currency: String,
    val exchange_rate: Double,
    val message: String,
    val order_id: Int,
    val price_in_usd: String
)