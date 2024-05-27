package com.amtech.baseetcustomer.SignUp.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ModelSignUp(
    val is_phone_verified: Int,
    val phone_verify_end_url: String,
    val token: String
)
data class ErrorResponse(
    @SerializedName("errors") val errors: List<Error>
)

data class Error(
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)

// Function to parse the JSON response and print the error messages
fun handleErrorResponse(jsonResponse: String) {
    val gson = Gson()
    val errorResponse = gson.fromJson(jsonResponse, ErrorResponse::class.java)

    errorResponse.errors.forEach { error ->
        println("Code: ${error.code}, Message: ${error.message}")
    }
}
