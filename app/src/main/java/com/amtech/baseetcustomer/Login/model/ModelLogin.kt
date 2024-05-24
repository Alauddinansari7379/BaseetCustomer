package com.amtech.baseetcustomer.Login.model

data class ModelLogin(
    val is_phone_verified: Int,
    val token: String,
    val user_id: Int
)