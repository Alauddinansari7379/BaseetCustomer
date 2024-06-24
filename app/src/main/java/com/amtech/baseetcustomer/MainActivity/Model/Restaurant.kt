package com.amtech.baseetcustomer.MainActivity.Model

data class Restaurant(
    val active: Boolean,
    val address: String,
    val appcoverlogo: String,
    val applogo: String,
    val closeing_time: String,
    val comission: Any,
    val cover_photo: String,
    val created_at: String,
    val delivery: Boolean,
    val delivery_charge: Int,
    val delivery_time: String,
    val discount: String,
    val email: String,
    val food_section: Boolean,
    val footer_text: Any,
    val free_delivery: Boolean,
    val gst_code: String,
    val gst_status: Boolean,
    val id: Int,
    val latitude: String,
    val logo: String,
    val longitude: String,
    val m_choice: Any,
    val minimum_order: Int,
    val name: String,
    val non_veg: Int,
    val off_day: String,
    val opening_time: String,
    val order_count: Int,
    val phone: String,
    val pos_system: Boolean,
    val qrcode_img: Any,
    val rating: List<Int>,
    val req_id: String,
    val reviews_section: Boolean,
    val schedule_order: Boolean,
    val self_delivery_system: Int,
    val status: Int,
    val take_away: Boolean,
    val tax: Int,
    val total_order: Int,
    val type: String,
    val updated_at: String,
    val veg: Int,
    val vendor_id: Int,
    val zone_id: Int
)